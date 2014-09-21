package com.newfivefour.natcher.uicomponent.events;

public interface OnEmptyConnector {
    /**
     * Should be called after the empty display is set, so this can
     * be passed to that.
     */
    void setEmptyCallback(OnEmptyCallback callback);
}
