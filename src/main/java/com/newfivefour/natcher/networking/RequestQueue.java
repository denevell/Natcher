package com.newfivefour.natcher.networking;

import android.os.Bundle;
import android.util.Log;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * UUID strings added to the current fetches, and removed on completion.
 *
 * This means a request with a unique UUID won't be called until the previous one completed.
 *
 * Useful for ensuring the request doesn't start again on screen rotation, but waits instead.
 */
public class RequestQueue {

    private static CopyOnWriteArraySet<String> sCurrentFetches = new CopyOnWriteArraySet<String>();
    private static String TAG = RequestQueue.class.getSimpleName();

    public static String getUuidFromBundle(Bundle requestUuidBundle) {
        if(requestUuidBundle==null) {
            Log.d(TAG, "Request uuid bundle is null");
            return null;
        }
        String existingRequestUuid = null;
        existingRequestUuid = requestUuidBundle.getString("REQUEST_UUID");
        if (existingRequestUuid==null) {
            Log.d(TAG, "Generating a new request uuid");
            String uuid = UUID.randomUUID().toString();
            existingRequestUuid = uuid;
        } else {
            Log.d(TAG, "Existing request UUID: " + existingRequestUuid);
        }
        requestUuidBundle.putString("REQUEST_UUID", existingRequestUuid);
        return existingRequestUuid;
    }

    public static boolean isRequestUnderway(String requestUuid) {
        if(requestUuid!=null && sCurrentFetches!=null && sCurrentFetches.contains(requestUuid)) {
            Log.d(TAG, "Request is underway");
            return true;
        } else {
            Log.d(TAG, "Request is not underway");
            return false;
        }
    }

    public static void addRequestUuid(String requestUuid) {
        if(sCurrentFetches!=null) {
            sCurrentFetches.add(requestUuid);
        }
    }

    public static void removeUuid(String requestUuid) {
        if(sCurrentFetches!=null) {
            sCurrentFetches.remove(requestUuid);
        }
    }
}
