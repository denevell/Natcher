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

public class MessageBusService<ReturnResult, ServiceClass> {

    private static final String TAG = MessageBusService.class.getSimpleName();
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

    public void fetch(String endPoint,
                      Class<ServiceClass> serviceClass,
                      GetResult<ReturnResult, ServiceClass> getResult,
                      ErrorResponse errorResponse,
                      CachedResponse<ReturnResult> cacheResponse,
                      String cacheResponseUri,
                      Bundle bundleForRequestUuid,
                      Class<? extends ReturnResult> returnType) {
        fetch(endPoint,
                serviceClass,
                new GsonConverter(new Gson()),
                getResult,
                errorResponse,
                cacheResponse,
                cacheResponseUri,
                bundleForRequestUuid,
                returnType);
    }

    /**
     * Fetches an object from an end point
     *
     * @param endPoint      url
     * @param serviceClass  Retrofit class
     * @param errorResponse Object we'll send result of an error
     * @param converter     The Retrofit converter, GSON normally
     * @param getResult     Callback that uses the retrofit class to grab the return value
     */
    public void fetch(final String endPoint,
                      Class<ServiceClass> serviceClass,
                      Converter converter,
                      GetResult<ReturnResult, ServiceClass> getResult,
                      ErrorResponse errorResponse,
                      CachedResponse<ReturnResult> cacheResponse,
                      String cachedResponseUri,
                      Bundle requestUuidBundle,
                      Class<? extends ReturnResult> returnType) {

        OkClient ok = createHttpClient();
        ServiceClass service = createServiceClass(endPoint, serviceClass, converter, ok);
        returnCacheIfAvailable(endPoint, cacheResponse, cachedResponseUri, returnType);

        String requestUuid = RequestQueue.getUuidFromBundle(requestUuidBundle);
        if(RequestQueue.isRequestUnderway(requestUuid)) {
            Log.d(TAG, "We're already fetching it apparently.");
        } else {
            returnNetworkResponse(requestUuid, endPoint, getResult, errorResponse, service);
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
                } else if (res == null && errorResponse != null) {
                    Log.d(TAG, "onPostExecute(): Sending an error back");
                    Application.getEventBus().post(errorResponse);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void returnCacheIfAvailable(final String endPoint, final CachedResponse<ReturnResult> cacheResponse, final String cachedResponseUri, final Class<? extends ReturnResult> returnType) {
        if(cachedResponseUri!=null) {
            new AsyncTask<Void, Void, ReturnResult>() {
                @Override
                protected ReturnResult doInBackground(Void... params) {
                    try {
                        ReturnResult cached = ResponseCache.getObject(endPoint + cachedResponseUri, returnType);
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

