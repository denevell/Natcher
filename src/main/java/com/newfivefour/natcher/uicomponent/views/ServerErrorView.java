package com.newfivefour.natcher.uicomponent.views;

import com.newfivefour.natcher.uicomponent.events.OnRefreshConnector;

/**
 * A View, for example, that can toggle it's display depending on
 * the showServerError() method herein.
 */
public interface ServerErrorView extends OnRefreshConnector {
    void showServerError(boolean show);
}
