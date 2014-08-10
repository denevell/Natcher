package com.newfivefour.natcher.networking;

import android.os.Bundle;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

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
