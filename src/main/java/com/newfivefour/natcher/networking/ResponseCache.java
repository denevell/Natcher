package com.newfivefour.natcher.networking;

import com.google.gson.Gson;
import com.newfivefour.natcher.app.Application;
import com.squareup.okhttp.internal.DiskLruCache;
import com.squareup.okhttp.internal.Util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Gets the response body cache that OkHTTP set.
 */
public class ResponseCache {
    public static final int SIZE_OF_RESPONSE_CACHE = 10000 * 5;

    private static FilterInputStream getFromCache(String url) throws Exception {
        // WARNING: The 201105 and 2 values come directly from the OkHTTP code - when they change, this should too.
        DiskLruCache cache = DiskLruCache.open(Application.getContext().getCacheDir(), 201105, 2, SIZE_OF_RESPONSE_CACHE);
        cache.flush();
        String key = Util.hash(url);
        final DiskLruCache.Snapshot snapshot;
        try {
            snapshot = cache.get(key);
            if (snapshot == null) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }

        FilterInputStream bodyIn = new FilterInputStream(snapshot.getInputStream(1)) {
            @Override
            public void close() throws IOException {
                snapshot.close();
                super.close();
            }
        };

        return bodyIn;
    }

    public static <T> T getObject(String fullUrl, Class<T> type) {
        try {
            FilterInputStream cached = ResponseCache.getFromCache(fullUrl);
            Scanner sc = new Scanner(cached);
            String str="", s;
            while(sc.hasNext() && (s=sc.nextLine())!=null) {
                str = str + s;
            }
            T t = new Gson().fromJson(str, type);
            return t;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
