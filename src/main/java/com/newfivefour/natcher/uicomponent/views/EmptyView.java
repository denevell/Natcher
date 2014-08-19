package com.newfivefour.natcher.uicomponent.views;

import com.newfivefour.natcher.uicomponent.events.OnEmptyConnector;
import com.newfivefour.natcher.uicomponent.events.OnRefreshConnector;

/**
 * A View, for example, that can toggle it's display depending on
 * the showEmpty() method herein.
 *
 * It also takes in OnEmptyConnector and OnRefreshConnector so it can call
 * such callbacks in its view if it wants.
 */
public interface EmptyView extends OnEmptyConnector, OnRefreshConnector {
    void showEmpty(boolean empty);
}
