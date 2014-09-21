package com.newfivefour.natcher.uicomponent.events;

public interface OnRefreshWidget {
    /**
     * SHould be called after the error, empty and server error views are
     * set so they can be given this callback
     */
    void setRefreshableCallback(OnRefreshCallback callback);
}
