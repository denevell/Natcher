package com.newfivefour.natcher.uicomponent;

import com.newfivefour.natcher.uicomponent.events.OnEmptyConnector;
import com.newfivefour.natcher.uicomponent.events.OnRefreshConnector;
import com.newfivefour.natcher.uicomponent.views.LoadingView;

/**
 * Allows your component to enjoy all the goodness of a UiComponent
 * i.e. dealing with loading views, error views, empty views, cached content,
 * overall loading view, refreshing from server error, actions on empty content,
 *
 * @see com.newfivefour.natcher.uicomponent.UiComponentVanilla
 */
public interface UiComponent<T> extends
        OnRefreshConnector,
        OnEmptyConnector {

    void setHideKeyboard(Runnable runnable);

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

    /**
     * The component may want to set a loader for the overall page, in addition to
     * the loader is this component.
     *
     * If it's set, it only shows when there's cached content, and we're refreshing.
     */
    UiComponent<T> setPageWideLoadingDisplay(LoadingView loadingView);

    void populateStarting();
    void populateFromCache(T ob);
    void populateFromServer(T ob);
    void populateFromServerError(int responseCode);
    void populateWithEmptyContentFromServer();
    void populateWithEmptyContentFromCache();
}
