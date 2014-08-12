package com.newfivefour.natcher.networking;

import android.util.Log;

import com.google.gson.Gson;
import com.jakewharton.disklrucache.DiskLruCache;
import com.newfivefour.natcher.app.Application;
import com.squareup.okhttp.internal.Util;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * Caches the response objects.
 *
 * Doesn't use the OkHTTP cache, since if you use that, and get and put and object at the same time: goodbye cache.
 *
 * And since OkHTTP's cache object is 'final' you can extend it to synchronise access.
 */
public class ResponseCache {
    public static final int SIZE_OF_RESPONSE_CACHE = 10000 * 5;
    private static final String TAG = ResponseCache.class.getSimpleName();
    private static DiskLruCache cache;
    private static Object sSyncLock = new Object();

    private static FilterInputStream getFromCache(String url) throws Exception {
        if(cache==null) {
            File filesDir = Application.getContext().getFilesDir();
            String dir = filesDir.getAbsolutePath()+"/natcher_response_cache";
            File cacheDir = new File(dir);
            cacheDir.mkdirs();
            cache = DiskLruCache.open(cacheDir, 201105, 1, SIZE_OF_RESPONSE_CACHE);
        }
        cache.flush();
        String key = Util.hash(url);
        final DiskLruCache.Snapshot snapshot;
        try {
            synchronized (sSyncLock) {
                snapshot = cache.get(key);
            }
            if (snapshot == null) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        FilterInputStream bodyIn = new FilterInputStream(snapshot.getInputStream(0)) {
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
            if(cached==null) {
                cached = ResponseCache.getFromCache(fullUrl);
                if(cached==null) {
                    Log.e(TAG, "Can't find snapshot after retrying once");
                    return null;
                }
            }
            Scanner sc = new Scanner(cached);
            String str="", s;
            while(sc.hasNext() && (s=sc.nextLine())!=null) {
                str = str + s;
            }
            T t = new Gson().fromJson(str, type);
            return t;
        } catch(Exception e) {
            Log.d(TAG, "Error getting object", e);
            return null;
        }
    }

   public static <T> void put(String fullUrl, T ob, Class<? extends T> typeClass) throws IOException {
        DiskLruCache.Editor editor = null;
        try {
            synchronized (sSyncLock) {
                editor = cache.edit(Util.hash(fullUrl));
                if (editor == null) {
                    Log.d(TAG, "Editor was null on adding to cache");
                    return;
                }
                OutputStream bos = editor.newOutputStream(0);
                String s = new Gson().toJson(ob, typeClass);
                bos.write(s.getBytes());
                bos.flush();
            }
        } catch (IOException e) {
            Log.d(TAG, "Exception while adding to cache", e);
        } finally {
            if(editor!=null) {
                editor.commit();
            }
        }
    }
}
