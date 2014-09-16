package com.newfivefour.natcher.uicomponent;

import android.util.Log;

import com.newfivefour.natcher.uicomponent.events.OnEmpty;
import com.newfivefour.natcher.uicomponent.events.OnRefresh;
import com.newfivefour.natcher.uicomponent.events.OnRefreshConnector;
import com.newfivefour.natcher.uicomponent.views.EmptyView;
import com.newfivefour.natcher.uicomponent.views.LoadingView;
import com.newfivefour.natcher.uicomponent.views.ServerErrorView;

/**
 * Allows your component to enjoy all the goodness of a UiComponent
 * i.e. dealing with loading views, error views, empty views, cached content,
 * overall loading view, refreshing from server error, actions on empty content,
 *
 * In-component loader display
 * ###########################
 *
 * # Given the component loading call has been called
 * Given we have no cached content
 * Then show the in-component loading display
 *
 * # Given the component has been given cached content
 * Then remove the in-component loading display
 *
 * # Given the component has been given server content
 * Then remove the in-component loading display
 *
 * # Given the component has received a server error
 * Then remove the in-component loading display
 *
 * # Given the component has empty content
 * Then remove the in-component loading display
 *
 * # Given we have received empty content from server
 * Then remove the in-component loading display
 *
 * # Given we have received empty content from cache
 * Then remove the in-component loading display
 *
 * Overall page-loader display
 * ###########################
 *
 * Given populate loading is called
 * Given there is content in the component
 * Then the overall page loading display should be set active
 *
 * Given the cache returns good content
 * Given a service hasn't returned data
 * Then the overall page loading display should be set active
 *
 * Given  the cache return empty content
 * Given a service hasn't returned data
 * Then the overall page loading display should be set active
 *
 * Given the server returns good content, an error or empty content
 * Then the overall page loading display should be removed
 *
 * Given populate loading is called
 * Given there is no content is the component
 * Then the overall page loading display should be removed
 *
 * Server error
 * ############
 *
 * # Given a server error
 * Given we have no cached content
 * Then we show the in-component error view
 *
 * # Given a server error
 * Given we have cached content
 * Show the error view for showing when cached content
 *
 * # Given we have received new server content
 * Then hide both server error types
 *
 * # Given we have received cached content
 * Then hide both server error types
 *
 * # Given we have received empty content from server
 * Then hide both server error types
 *
 * # Given we have received empty content from cache
 * Then hide both server error types
 *
 * # Given a loading start call
 * Then hide both server error types
 *
 * Empty content
 * #############
 *
 * # Given the data is deemed 'empty' from server
 * Then clear content
 * Then show the empty display
 *
 * # Given the data is deemed 'empty' from cache
 * Then clear content
 * Then show the empty display
 *
 * # Given the component has been given cached content
 * Then remove the empty display
 *
 * # Given the component has been given server content
 * Then remove the empty display
 *
 * # Given the component has received a server error
 * Then remove the empty display
 *
 * Given a loading start call
 * Then hide the empty display
 *
 * Refresh callback
 * #################
 *
 * Given we set a refreshing callback
 * Then the widget that deals with errors and empty views is given that
 *
 * Is empty callback
 * #################
 *
 * Given we set a is empty callback
 * Then the widget that deals with errors and empty views is given that
 *
 * Keyboard hider
 * #################
 *
 * Given we start populating
 * Then we should hide the keyboard if callback there
 *
 */
public class UiComponentVanilla<T> implements UiComponent<T> {
    private static String TAG = UiComponentVanilla.class.getSimpleName();
    private OnRefreshConnector mRefreshWidget;

    private LoadingView mPageWideLoader;
    private LoadingView mInComponentLoading;
    private EmptyView mEmptyView;
    private ServerErrorView mServerErrorView;
    private ServerErrorView mServerErrorViewWhenWeHaveContent;
    private Runnable mHideKeyboardCallback;

    /**
     * Used to communicate with whatever is using this as a delegate
     */
    private Populatable<T> mPopulatable = null;

    /**
     * We don't need to store this to be given to this again on creation if we're giving this again on creation as default false,
     * and later true when loading starts.
     *
     * And we will also get that again, since our requests will fire again, thereby calling start loading.
     *
     * This isn't a problem, per se, since our networking implementation won't be sending out the same requests again,
     * just finishing existing ones.
     */
    private boolean mPageWideLoadingStarted = false;

    /**
     * We, as above, don't need to store this to be given to this again on creation.
     *
     * This is because the true state will be here by default, and you'll want that
     * on a creation, since you'll be getting the data again.
     */
    private boolean mShouldStartPageLoaderAfterCachedResult = true;

    public UiComponentVanilla(Populatable<T> populatable) {
        mPopulatable = populatable;
    }

    public UiComponentVanilla<T> setInComponentLoadingDisplay(LoadingView loadingView) {
        mInComponentLoading = loadingView;
        return this;
    }

    public UiComponentVanilla<T> setInComponentEmptyDisplay(EmptyView emptyView) {
        mEmptyView = emptyView;
        return this;
    }

    /**
     * 1. So, we have this view. A normal custom view.
     *
     * 2. It has a delegate interface.
     *
     * This means that, like many other views, other will be able to access an object
     * it has: the object allows the caller to manipulate the view.
     *
     * 3. There are two particular methods on this component that allow the caller to
     * specify something happens on an event.
     *
     *  3.1. One of these methods allows the caller to say something should happen when
     *  the component presses refresh.
     *
     *  3.2 Another one of these methods allows the caller to say something should the
     * component have an empty view.
     */

    public UiComponentVanilla<T> setInComponentServerErrorDisplay(ServerErrorView errorComponent) {
        mServerErrorView = errorComponent;
        return this;
    }

    public UiComponentVanilla<T> setInComponentServerErrorDisplayForUseWhenWeHaveContent(ServerErrorView errorComponent) {
        mServerErrorViewWhenWeHaveContent = errorComponent;
        return this;
    }

    public UiComponentVanilla<T> setPageWideLoadingDisplay(LoadingView loadingView) {
        Log.d(TAG, "setPageWideLoadingDisplay()");
        mPageWideLoader = loadingView;
        return this;
    }

    /**
     * @param refreshWidget This will be given the OnRefresh callback when it's set on the UiComponent
     */
    public UiComponentVanilla<T> setRefreshWidget(OnRefreshConnector refreshWidget) {
        mRefreshWidget = refreshWidget;
        return this;
    }

    public void setHideKeyboard(Runnable runnable) {
        Log.d(TAG, "setHideKeyboard()");
        mHideKeyboardCallback = runnable;
    }

    @Override
    public void setEmptyConnector(OnEmpty connector) {
        Log.d(TAG, "setEmptyConnector()");
        if(mEmptyView !=null) {
            mEmptyView.setEmptyConnector(connector);
        }
    }

    @Override
    public void setRefreshableConnector(OnRefresh connector) {
        Log.d(TAG, "setRefreshableConnector()");
        if(mEmptyView !=null) {
            mEmptyView.setRefreshableConnector(connector);
        }
        if(mServerErrorView !=null) {
            mServerErrorView.setRefreshableConnector(connector);
        }
        if(mRefreshWidget!=null) {
            mRefreshWidget.setRefreshableConnector(connector);
        }
    }

    /**
     * Hide:
     * 1. All loaders
     */
    @Override
    public void onResetComponent() {
        // This will only do so if it's active
        setPageWideLoading(false);
        // Not really needed, since we're resetting the component, but may be useful if
        // the loading is outside the component (naughty)
        setLoading(false);
    }

    /**
     * Show:
     * 1. In or out of component loading depending on callback
     *
     * Hide:
     * 1. Empty displays
     * 2. Server error displays
     * 3. Keyboard
     */
    @Override
    public void populateStarting() {
        Log.d(TAG, "populateStarting()");
        mShouldStartPageLoaderAfterCachedResult = true;
        setEmptyError(false);
        hideServerErrors();
        hideKeyboard();
        if(mPopulatable.shouldShowInComponentLoadingInsteadOfOutOfComponent()) {
            Log.d(TAG, "populateStarting(): set in-component loading");
            setLoading(true);
        } else {
            Log.d(TAG, "populateStarting(): set page-wide loading");
            setPageWideLoading(true);
        }
    }

    /**
     * Give the content to the view to populate
     *
     * Show:
     * 1. Out-of-component loading (if we're before the server return and a callback says it's okay)
     *
     * Hide:
     * 1. In-component loading
     * 2. Server error displays
     * 3. Empty displays
     *
     */
    @Override
    public void populateFromCache(T ob) {
        Log.d(TAG, "populateFromCache()");
        mPopulatable.populateOnSuccessfulResponse(ob);
        setLoading(false);
        hideServerErrors();
        setEmptyError(false);
        if(mShouldStartPageLoaderAfterCachedResult && !mPopulatable.shouldShowInComponentLoadingInsteadOfOutOfComponent()) {
            Log.d(TAG, "populateFromCache(): setPageWideLoading");
            setPageWideLoading(true);
        }
    }

    /**
     * Give the content to the view to populate
     *
     * Hide:
     * 1. All loading
     * 2. Server displays
     * 3. Empty displays
     */
    @Override
    public void populateFromServer(T ob) {
        Log.d(TAG, "populateFromServer()");
        mShouldStartPageLoaderAfterCachedResult = false;
        mPopulatable.populateOnSuccessfulResponse(ob);
        setPageWideLoading(false);
        setLoading(false);
        hideServerErrors();
        setEmptyError(false);
    }

    /**
     * Show the server error
     *
     * Show:
     * 1. Show the server error, in component or out of component, depending on callback
     *
     * Hide:
     * 1. All loading
     * 3. Empty displays
     */
    @Override
    public void populateFromServerError(int responseCode) {
        Log.d(TAG, "populateFromServerError()");
        mShouldStartPageLoaderAfterCachedResult = false;
        if(mPopulatable.shouldShowServerErrorInComponentOrOutOfComponent()) {
            Log.d(TAG, "populateFromServerError(): empty view content");
            setServerError(true);
        } else {
            Log.d(TAG, "populateFromServerError(): non empty view content");
            setServerError(false);
            setServerErrorForUseWithCachedContent(true);
        }
        setPageWideLoading(false);
        setLoading(false);
        setEmptyError(false);
    }

    /**
     * Clear the content when we get a empty cache response
     *
     * Show:
     * 1. Empty view
     *
     * Hide:
     * 1. In=component loading
     * 2. Server errors
     *
     * Leave:
     * 1. Out of component loading
     */
    @Override
    public void populateWithEmptyContentFromCache() {
        Log.d(TAG, "populateWithEmptyContentFromCache()");
        mPopulatable.clearContentWhenServerReturnsEmptyResponse();
        setLoading(false);
        hideServerErrors();
        setEmptyError(true);
    }

    /**
     * Show empty view when response from server
     *
     * Show:
     * 1. Empty view
     *
     * Hide:
     * 1. All loading
     * 2. Server errors
     */
    @Override
    public void populateWithEmptyContentFromServer() {
        Log.d(TAG, "populateWithEmptyContentFromServer()");
        mPopulatable.clearContentWhenServerReturnsEmptyResponse();
        setPageWideLoading(false);
        setLoading(false);
        hideServerErrors();
        setEmptyError(true);
    }

    private void setLoading(boolean show) {
        if(mInComponentLoading!=null) {
            mInComponentLoading.showLoading(show);
        }
    }

    private void hideServerErrors() {
        setServerError(false);
        setServerErrorForUseWithCachedContent(false);
    }

    private void setServerError(boolean show) {
        if(mServerErrorView !=null) {
            mServerErrorView.showServerError(show);
        }
    }

    private void setServerErrorForUseWithCachedContent(boolean show) {
        if(mServerErrorViewWhenWeHaveContent !=null) {
            mServerErrorViewWhenWeHaveContent.showServerError(show);
        }
    }

    private void setEmptyError(boolean show) {
        if(mEmptyView !=null) {
            mEmptyView.showEmpty(show);
        }
    }

    private void setPageWideLoading(boolean show) {
        if(mPageWideLoader !=null) {
            if(show && mPageWideLoadingStarted) {
                return;
            }
            if(!show && !mPageWideLoadingStarted) {
                return;
            }
            mPageWideLoadingStarted = show;
            mPageWideLoader.showLoading(show);
        }
    }

    private void hideKeyboard() {
        if(mHideKeyboardCallback!=null) {
            mHideKeyboardCallback.run();
        }
    }


}
