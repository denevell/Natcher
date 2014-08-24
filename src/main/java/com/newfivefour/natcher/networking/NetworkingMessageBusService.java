package com.newfivefour.natcher.networking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.newfivefour.natcher.app.Application;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Takes in a Retrofit service class and returns its result down the Otto event bus.
 *
 * Can perform
 * 1. Response body caching,
 * 2. Waits for a unique request to complete before sending another,
 * 4. Sending events on error from server
 * 5. Sending events on cache from server
 * 6. Sending events on empty content from server
 * 7. Returning cached responses if Etags or the like are used.
 */
public class NetworkingMessageBusService<ReturnResult, ServiceClass> {

    private static final String TAG = NetworkingMessageBusService.class.getSimpleName();
    private static final int SIZE_OF_RESPONSE_CACHE = 10000 * 5;
    private final CachedResponse<ReturnResult> mCacheResponse;
    private final String mCachedResponseUri;
    private final Bundle mRequestUuidBundle;
    private final IsEmpty mIsEmptyCallback;
    private final ResponseEmpty mIsEmptyObject;
    private final Converter mConverter;

    public static interface GetResult<ReturnResult, ServiceClass> {
        public ReturnResult getResult(ServiceClass mService) throws Exception;
    }

    public static interface IsEmpty<ReturnResult> {
        public boolean isEmpty(ReturnResult result);
    }

    public static class CachedResponse<ReturnResult> {
        private ReturnResult mRes;

        public CachedResponse<ReturnResult> setCache(ReturnResult res) {
            mRes = res;
            return this;
        }
        public ReturnResult returnCached() {
            return mRes;
        }
    }

    public static class Builder<ReturnResult, ServiceClass> {
        private Converter converter = new GsonConverter(new Gson());
        private CachedResponse<ReturnResult> cacheResponse;
        private String cachedResponseUri;
        private Bundle requestUuidBundle;
        private ResponseEmpty isEmptyObject;
        private IsEmpty isEmptyCallback;

        /**
        * @param converter The Retrofit mConverter, GsonConverter normally and by default
        */
        public Builder converter(Converter converter) {
            this.converter = converter;
            return this;
        }

        /**
         * If you want to send a cached response back down the event bus.
         *
         * OkHTTP does the response caching for us.
         * @param requestUriMinusBase Used to find the cached response
         * @param callback The object that will be sent back down the event bus
         */
        public Builder cacheRequest(String requestUriMinusBase, CachedResponse<ReturnResult> callback) {
            this.cachedResponseUri = requestUriMinusBase;
            this.cacheResponse = callback;
            return this;
        }

        /**
         * @param bundleWithUuid If the bundle contains a UUID that is being processed, a new request won't touch the network, but will
         *               return the cache and wait for the request to finish.
         *
         *               Once the request is finished, the UUID will no longer match currently processing requests, and a new
         *               request will again touch the network.
         *
         *               If there is no UUID in the bundle, we'll add one to it.
         *
         *               This is especially useful for Fragment.getArguments(), which persist on Fragment recreation.
         */
        public Builder dontRerequestExistingRequest(Bundle bundleWithUuid) {
            this.requestUuidBundle = bundleWithUuid;
            return this;
        }

        /**
         * Set this if you want to return a specific object if the server's response is empty
         * @return
         */
        public Builder detectEmptyResponse(IsEmpty<ReturnResult> isEmptyCallback, ResponseEmpty isEmptyObject) {
            this.isEmptyCallback = isEmptyCallback;
            this.isEmptyObject = isEmptyObject;
            return this;
        }

        public NetworkingMessageBusService<ReturnResult, ServiceClass> create() {
            NetworkingMessageBusService<ReturnResult, ServiceClass> service = new NetworkingMessageBusService<ReturnResult, ServiceClass>(
                    cacheResponse,
                    cachedResponseUri,
                    requestUuidBundle,
                    isEmptyCallback,
                    isEmptyObject,
                    converter
            );
            return service;
        }

    }

    private NetworkingMessageBusService(
            CachedResponse<ReturnResult> cacheResponse,
            String cachedResponseUri,
            Bundle requestUuidBundle,
            IsEmpty isEmptyCallback,
            ResponseEmpty isEmptyObject,
            Converter converter) {
        this.mCacheResponse = cacheResponse;
        this.mCachedResponseUri = cachedResponseUri;
        this.mRequestUuidBundle = requestUuidBundle;
        this.mIsEmptyCallback = isEmptyCallback;
        this.mIsEmptyObject = isEmptyObject;
        this.mConverter = converter;
    }


    /**
     * @param bundle The Bundle you passed in with fetch()
     */
    public boolean isRequestUnderway(Bundle bundle) {
        String requestUuid = RequestQueue.getUuidFromBundle(bundle);
        return RequestQueue.isRequestUnderway(requestUuid);
    }

    /**
     * Fetches an object from an end point
     *
     * @param baseUrl      url
     * @param serviceClass  Retrofit class
     * @param getResult     Callback that uses the retrofit class to grab the return value
     * @param errorResponse Object we'll send result of an error
     */
    public void fetch(final String baseUrl,
                      Class<ServiceClass> serviceClass,
                      GetResult<ReturnResult, ServiceClass> getResult,
                      ResponseError errorResponse,
                      Class<? extends ReturnResult> returnType) {

        OkClient ok = createHttpClient();
        ServiceClass service = createServiceClass(baseUrl, serviceClass, mConverter, ok);

        // Cached response
        returnCacheIfAvailable(
                    baseUrl+ mCachedResponseUri,
                mCacheResponse,
                mIsEmptyCallback,
                mIsEmptyObject,
                    returnType);

        // Server response
        if(isRequestUnderway(mRequestUuidBundle)) {
            Log.d(TAG, "We're already fetching it apparently.");
        } else {
            String requestUuid = RequestQueue.getUuidFromBundle(mRequestUuidBundle);
            returnNetworkResponse(
                    baseUrl,
                    mCachedResponseUri,
                    requestUuid,
                    getResult,
                    errorResponse,
                    mIsEmptyCallback,
                    mIsEmptyObject,
                    service,
                    returnType);
        }
    }

    private void returnNetworkResponse(final String endPoint,
                                       final String cachedResponseUri,
                                       final String requestUuid,
                                       final GetResult<ReturnResult, ServiceClass> getResult,
                                       final ResponseError errorResponse,
                                       final IsEmpty<ReturnResult> isEmptyCallback,
                                       final ResponseEmpty isEmptyResponse,
                                       final ServiceClass service,
                                       final Class<? extends ReturnResult> returnType) {
        RequestQueue.addRequestUuid(requestUuid);
        new AsyncTask<Void, Void, ReturnResult>() {
            @Override
            protected ReturnResult doInBackground(Void... params) {
                // Now get from network
                try {
                    Log.d(TAG, "Attempting to fetch result from base url: " + endPoint);
                    ReturnResult res = getResult.getResult(service);
                    if(cachedResponseUri!=null) {
                        ResponseCache.put(endPoint+cachedResponseUri, res, returnType);
                    }
                    if (res != null) {
                        Log.d(TAG, "Fetched : " + res.toString() + " from " + endPoint);
                    }
                    return res;
                } catch (RetrofitError e) {
                    Log.d(TAG, "Caught a RetrofitError", e);
                    if (e.getResponse() != null && errorResponse != null) {
                        Log.d(TAG, "Filling error from response");
                        errorResponse.fill(e.getResponse().getStatus(),
                                e.getResponse().getReason(),
                                e.getResponse().getUrl(),
                                e.isNetworkError());
                    } else {
                        Log.d(TAG, "Not filling error from response - likely network down");
                    }
                    return null;
                } catch (Exception e1) {
                    Log.d(TAG, "Caught an Exception");
                    Log.e(TAG, "Unknown error", e1);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ReturnResult res) {
                Log.d(TAG, "onPostExecute()");
                RequestQueue.removeUuid(requestUuid);
                if(res != null && isEmptyCallback!=null && isEmptyResponse!=null && isEmptyCallback.isEmpty(res)) {
                    Log.d(TAG, "onPostExecute() detected empty response.");
                    isEmptyResponse.setIsFromCache(false);
                    Application.getEventBus().post(isEmptyResponse);
                } else if (res != null) {
                    Log.d(TAG, "onPostExecute(): Sending good result back");
                    super.onPostExecute(res);
                    Application.getEventBus().post(res);
                } else if (errorResponse != null) {
                    Log.d(TAG, "onPostExecute(): Sending an error back");
                    Application.getEventBus().post(errorResponse);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void returnCacheIfAvailable(
            final String fullCachedUrl,
            final CachedResponse<ReturnResult> cacheResponse,
            final IsEmpty<ReturnResult> isEmptyCallback,
            final ResponseEmpty isEmptyResponse,
            final Class<? extends ReturnResult> returnType) {
        if(cacheResponse!=null) {
            new AsyncTask<Void, Void, ReturnResult>() {
                @Override
                protected ReturnResult doInBackground(Void... params) {
                    try {
                        Log.d(TAG, "Attempting to send back cached version");
                        ReturnResult cached = ResponseCache.getObject(fullCachedUrl, returnType);
                        Log.d(TAG, "Sending back cached version");
                        return cached;
                    } catch(Exception e) {
                        Log.d(TAG, "Problem getting from cache", e);
                        return null;
                    }
                }
                @Override
                protected void onPostExecute(ReturnResult res) {
                    Log.d(TAG, "onPostExecute() (cached)");
                    if(res!=null && isEmptyCallback!=null && isEmptyResponse!=null && isEmptyCallback.isEmpty(res)) {
                        Log.d(TAG, "onPostExecute() detected cached empty response.");
                        isEmptyResponse.setIsFromCache(true);
                        Application.getEventBus().post(isEmptyResponse);
                    } else if(res!=null) {
                        Log.d(TAG, "onPostExecute() Sending back cached response initially.");
                        Application.getEventBus().post(cacheResponse.setCache(res));
                    } else {
                        Log.d(TAG, "onPostExecute() No cached result to send back.");
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private OkClient createHttpClient() {
        OkHttpClient http_client = new OkHttpClient();
        http_client.setSslSocketFactory(NukeSSLCerts.sc.getSocketFactory());
        try {
            Cache responseCache = new Cache(Application.getContext().getCacheDir(), SIZE_OF_RESPONSE_CACHE);
            http_client.setCache(responseCache);
        } catch (Exception e) {
            Log.d(TAG, "Unable to set http cache", e);
        }
        http_client.setReadTimeout(30, TimeUnit.SECONDS);
        http_client.setConnectTimeout(30, TimeUnit.SECONDS);
        return new OkClient(http_client);
    }

    private ServiceClass createServiceClass(String endPoint, Class<ServiceClass> serviceClass, Converter converter, OkClient ok) {
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .setConverter(converter)
                .setClient(ok)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("OkHTTP"))
                .build();
        return restAdapter.create(serviceClass);
    }

}

