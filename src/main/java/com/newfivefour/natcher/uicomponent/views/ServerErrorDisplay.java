package com.newfivefour.natcher.uicomponent.views;

import com.newfivefour.natcher.uicomponent.events.OnRefreshWidget;

/**
 * A View, for example, that can toggle it's display depending on
 * the showServerError() method herein.
 */
public interface ServerErrorDisplay extends OnRefreshWidget {
    /**
     * @param show
     * @param code 0 when show is false
     * @param message null when show is false
     */
    void showServerError(boolean show, int code, String message);
}
