package com.newfivefour.natcher.networking;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.newfivefour.natcher.Application;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.internal.DiskLruCache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
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

    public static interface ReturnResultAdapter<ReturnResult> {
        public void adapt(ReturnResult res);
    }

    private DiskLruCache getDiskLruCache() throws IOException {
        return DiskLruCache.open(Application.getContext().getCacheDir(), 201105, 2, SIZE_OF_RESPONSE_CACHE);
    }

    public void fetch(String endPoint,
                      Class<ServiceClass> serviceClass,
                      final GetResult<ReturnResult, ServiceClass> getResult,
                      ErrorResponse errorResponse,
                      Class<? extends ReturnResult> returnType) {
        fetch(endPoint, serviceClass, new GsonConverter(new Gson()), getResult, errorResponse, returnType);
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
                      final GetResult<ReturnResult, ServiceClass> getResult,
                      final ErrorResponse errorResponse,
                      final Class<? extends ReturnResult> returnType) {

        final OkClient ok = createHttpClient();
        final ServiceClass service = createServiceClass(endPoint, serviceClass, converter, ok);

        // Call the service in an async task, sending the success or error to the event bus
        new AsyncTask<Void, Void, ReturnResult>() {
            @Override
            protected ReturnResult doInBackground(Void... params) {
                // Now get from network
                try {
                    Log.d(TAG, "Attempting to fetch result from base url: " + endPoint);
                    ReturnResult res = getResult.getResult(service);
                    if (res != null) {
                        Log.d(TAG, "Fetched : " + res.toString() + " from " + endPoint);
                    }
                    return res;
                } catch (RetrofitError e) {
                    Log.d(TAG, "Caught a RetrofitError", e);
                    if (e.isNetworkError()) {
                    }
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
                if (res != null) {
                    Log.d(TAG, "onPostExecute(): Sending good result back");
                    super.onPostExecute(res);
                    Application.getEventBus().post(res);
                } else if (res == null && errorResponse != null) {
                    Log.d(TAG, "onPostExecute(): Sending an error back");
                    Application.getEventBus().post(errorResponse);
                }
            }
        }.execute();
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
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                        request.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("OkHTTP"))
                .build();
        return restAdapter.create(serviceClass);
    }

}

