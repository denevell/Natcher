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
 * Can perform response body caching, and waits for a unique request to complete before sending another.
 * @param <ReturnResult>
 * @param <ServiceClass>
 */
public class NetworkingMessageBusService<ReturnResult, ServiceClass> {

    private static final String TAG = NetworkingMessageBusService.class.getSimpleName();
    private static final int SIZE_OF_RESPONSE_CACHE = 10000 * 5;

    public static interface GetResult<ReturnResult, ServiceClass> {
        public ReturnResult getResult(ServiceClass mService) throws Exception;
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

        /**
        * @param converter The Retrofit converter, GsonConverter normally and by default
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
        public Builder requestUuidBundle(Bundle bundleWithUuid) {
            this.requestUuidBundle = bundleWithUuid;
            return this;
        }

        /**
         * Fetches an object from an end point
         *
         * @param baseUrl      url
         * @param serviceClass  Retrofit class
         * @param errorResponse Object we'll send result of an error
         * @param getResult     Callback that uses the retrofit class to grab the return value
         */
        public void fetch(String baseUrl,
                     Class<ServiceClass> serviceClass,
                     GetResult<ReturnResult, ServiceClass> getResult,
                     ErrorResponse errorResponse,
                     Class<? extends ReturnResult> returnType) {
            NetworkingMessageBusService<ReturnResult, ServiceClass> service = new NetworkingMessageBusService<ReturnResult, ServiceClass>();
            service.fetch(baseUrl,
                    serviceClass,
                    converter,
                    getResult,
                    errorResponse,
                    cacheResponse,
                    cachedResponseUri,
                    requestUuidBundle,
                    returnType);
        }
    }

    private void fetch(final String baseUrl,
                      Class<ServiceClass> serviceClass,
                      Converter converter,
                      GetResult<ReturnResult, ServiceClass> getResult,
                      ErrorResponse errorResponse,
                      CachedResponse<ReturnResult> cacheResponse,
                      String cachedResponseUri,
                      Bundle requestUuidBundle,
                      Class<? extends ReturnResult> returnType) {

        OkClient ok = createHttpClient();
        ServiceClass service = createServiceClass(baseUrl, serviceClass, converter, ok);
        returnCacheIfAvailable(baseUrl+cachedResponseUri, cacheResponse, returnType);

        String requestUuid = RequestQueue.getUuidFromBundle(requestUuidBundle);
        if(RequestQueue.isRequestUnderway(requestUuid)) {
            Log.d(TAG, "We're already fetching it apparently.");
        } else {
            returnNetworkResponse(requestUuid, baseUrl, getResult, errorResponse, service);
        }
    }

    private void returnNetworkResponse(final String requestUuid, final String endPoint, final GetResult<ReturnResult, ServiceClass> getResult, final ErrorResponse errorResponse, final ServiceClass service) {
        new AsyncTask<Void, Void, ReturnResult>() {
            @Override
            protected ReturnResult doInBackground(Void... params) {
                // Now get from network
                try {
                    RequestQueue.addRequestUuid(requestUuid);
                    Log.d(TAG, "Attempting to fetch result from base url: " + endPoint);
                    ReturnResult res = getResult.getResult(service);
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
                if (res != null) {
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

    private void returnCacheIfAvailable(final String fullCachedUrl, final CachedResponse<ReturnResult> cacheResponse, final Class<? extends ReturnResult> returnType) {
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
                    if(res!=null) {
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

