package com.newfivefour.natcher.networking;

import android.os.Bundle;

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

    public static String getUuidFromBundle(Bundle requestUuidBundle) {
        String existingRequestUuid = null;
        if (requestUuidBundle!=null) {
            existingRequestUuid = requestUuidBundle.getString("REQUEST_UUID");
        }
        if (existingRequestUuid==null) {
            String uuid = UUID.randomUUID().toString();
            existingRequestUuid = uuid;
        }
        if (requestUuidBundle!=null) {
            requestUuidBundle.putString("REQUEST_UUID", existingRequestUuid);
        }
        return existingRequestUuid;
    }

    public static boolean isRequestUnderway(String requestUuid) {
        if(requestUuid!=null && sCurrentFetches!=null && sCurrentFetches.contains(requestUuid)) {
            return true;
        } else {
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
