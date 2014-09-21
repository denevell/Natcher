package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.events.OnEmptyConnector;
import com.newfivefour.natcher.uicomponent.events.OnRefreshWidget;

/**
 * Allows your component to enjoy all the goodness of a UiComponent
 * i.e. dealing with loading views, error views, empty views, cached content,
 * overall loading view, refreshing from server error, actions on empty content,
 *
 * We're extending OnRefreshConnector and OnEmptyConnector because the Fragment,
 * which will not have direct access to the implementation of UiComponent, may need
 * to do something regarding logic on refreshing the component, like calling a
 * service method, or finding it empty, like activating another ui element.
 *
 * @see com.newfivefour.natcher.uicomponent.UiComponentVanilla
 */
public interface UiComponent<T> extends OnRefreshWidget, OnEmptyConnector {

    /**
     * Called when the component is destroyed.
     *
     * Used to revert all external state changes it's made for itself, but not had
     * the chance to revert yet.
     *
     * An example is setting the overall app loading spinner active, but not had the
     * chance to dis-activate as yet, since the network hasn't returned.
     */
    void onResetComponent();

    void populateStarting();
    void populateFromCache(T ob);
    void populateFromServer(T ob);
    void populateFromServerError(int responseCode, String message);
    void populateWithEmptyContentFromServer();
    void populateWithEmptyContentFromCache();
}
