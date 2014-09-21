package com.newfivefour.natcher.uicomponent;

import android.util.Log;

import com.newfivefour.natcher.uicomponent.events.OnEmptyCallback;
import com.newfivefour.natcher.uicomponent.events.OnRefreshCallback;
import com.newfivefour.natcher.uicomponent.events.OnRefreshWidget;
import com.newfivefour.natcher.uicomponent.views.EmptyDisplay;
import com.newfivefour.natcher.uicomponent.views.LoadingDisplay;
import com.newfivefour.natcher.uicomponent.views.ServerErrorDisplay;

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
    private OnRefreshWidget mRefreshWidget;

    private LoadingDisplay mOutOfComponentLoader;
    private LoadingDisplay mInComponentLoader;
    private EmptyDisplay mEmptyDisplay;
    private ServerErrorDisplay mInComponentServerErrorDisplay;
    private ServerErrorDisplay mOutOfComponentServerErrorDisplay;
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
    private boolean mStartOutOfComponentLoaderAfterCachedResult = true;

    public UiComponentVanilla(Populatable<T> populatable) {
        mPopulatable = populatable;
    }

    public UiComponentVanilla<T> setInComponentLoadingDisplay(LoadingDisplay loadingDisplay) {
        mInComponentLoader = loadingDisplay;
        return this;
    }

    public UiComponentVanilla<T> setEmptyDisplay(EmptyDisplay emptyDisplay) {
        mEmptyDisplay = emptyDisplay;
        return this;
    }

    public UiComponentVanilla<T> setInComponentServerErrorDisplay(ServerErrorDisplay errorComponent) {
        mInComponentServerErrorDisplay = errorComponent;
        return this;
    }

    public UiComponentVanilla<T> setOutOfComponentServerErrorDisplay(ServerErrorDisplay errorComponent) {
        mOutOfComponentServerErrorDisplay = errorComponent;
        return this;
    }

    public UiComponentVanilla<T> setOutOfComponentLoadingDisplay(LoadingDisplay loadingDisplay) {
        Log.d(TAG, "setOutOfComponentLoadingDisplay()");
        mOutOfComponentLoader = loadingDisplay;
        return this;
    }

    /**
     * @param refreshWidget This will be given the OnRefresh callback when it's set on the UiComponent
     */
    public UiComponentVanilla<T> setRefreshWidget(OnRefreshWidget refreshWidget) {
        mRefreshWidget = refreshWidget;
        return this;
    }

    public void setHideKeyboard(Runnable runnable) {
        Log.d(TAG, "setHideKeyboard()");
        mHideKeyboardCallback = runnable;
    }

    @Override
    public void setEmptyCallback(OnEmptyCallback callback) {
        Log.d(TAG, "setEmptyCallback()");
        if(mEmptyDisplay !=null) {
            mEmptyDisplay.setEmptyCallback(callback);
        }
    }

    /**
     * Should be called after the empty, refresh, in component server errors are added
     */
    @Override
    public void setRefreshableCallback(OnRefreshCallback callback) {
        Log.d(TAG, "setRefreshableCallback()");
        if(mEmptyDisplay !=null) {
            mEmptyDisplay.setRefreshableCallback(callback);
        }
        if(mInComponentServerErrorDisplay !=null) {
            mInComponentServerErrorDisplay.setRefreshableCallback(callback);
        }
        if(mRefreshWidget!=null) {
            mRefreshWidget.setRefreshableCallback(callback);
        }
    }

    /**
     * Used so we can unset the out of component loader, to stop is loading forever.
     * Hide:
     * 1. All loaders
     */
    @Override
    public void onResetComponent() {
        // This will only do so if it's active
        setOutOfComponentLoading(false);
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
        mStartOutOfComponentLoaderAfterCachedResult = true;
        setEmptyError(false);
        hideServerErrors();
        hideKeyboard();
        if(mPopulatable.showInComponentLoading()) {
            Log.d(TAG, "populateStarting(): set in-component loading");
            setLoading(true);
        } else if(mPopulatable.showOutOfComponentLoading()) {
            Log.d(TAG, "populateStarting(): set page-wide loading");
            setOutOfComponentLoading(true);
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
        if(mStartOutOfComponentLoaderAfterCachedResult && mPopulatable.showOutOfComponentLoading()) {
            Log.d(TAG, "populateFromCache(): setOutOfComponentLoading");
            setOutOfComponentLoading(true);
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
        mStartOutOfComponentLoaderAfterCachedResult = false;
        mPopulatable.populateOnSuccessfulResponse(ob);
        setOutOfComponentLoading(false);
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
    public void populateFromServerError(int responseCode, String errorMessage) {
        Log.d(TAG, "populateFromServerError()");
        mStartOutOfComponentLoaderAfterCachedResult = false;
        if(mPopulatable.showInComponentServerError()) {
            Log.d(TAG, "populateFromServerError(): empty view content");
            setOutOfComponentServerError(false, 0, null);
            setInComponentServerError(true, responseCode, errorMessage);
        } else if(mPopulatable.showOutOfComponentServerError()) {
            Log.d(TAG, "populateFromServerError(): non empty view content");
            setInComponentServerError(false, 0, null);
            setOutOfComponentServerError(true, responseCode, errorMessage);
        }
        setOutOfComponentLoading(false);
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
        mPopulatable.clearContentOnEmptyResponse();
        setLoading(false);
        if(mStartOutOfComponentLoaderAfterCachedResult && mPopulatable.showOutOfComponentLoading()) {
            setOutOfComponentLoading(true);
        }
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
        mStartOutOfComponentLoaderAfterCachedResult = false;
        mPopulatable.clearContentOnEmptyResponse();
        setOutOfComponentLoading(false);
        setLoading(false);
        hideServerErrors();
        setEmptyError(true);
    }

    private void setLoading(boolean show) {
        if(mInComponentLoader !=null) {
            mInComponentLoader.showLoading(show);
        }
    }

    private void hideServerErrors() {
        setInComponentServerError(false, 0, null);
        setOutOfComponentServerError(false, 0, null);
    }

    private void setInComponentServerError(boolean show, int code, String message) {
        if(mInComponentServerErrorDisplay !=null) {
            mInComponentServerErrorDisplay.showServerError(show, code, message);
        }
    }

    private void setOutOfComponentServerError(boolean show, int code, String message) {
        if(mOutOfComponentServerErrorDisplay !=null) {
            mOutOfComponentServerErrorDisplay.showServerError(show, code, message);
        }
    }

    private void setEmptyError(boolean show) {
        if(mEmptyDisplay !=null) {
            mEmptyDisplay.showEmpty(show);
        }
    }

    private void setOutOfComponentLoading(boolean show) {
        if(mOutOfComponentLoader !=null) {
            if(show && mPageWideLoadingStarted) {
                return;
            }
            if(!show && !mPageWideLoadingStarted) {
                return;
            }
            mPageWideLoadingStarted = show;
            mOutOfComponentLoader.showLoading(show);
        }
    }

    private void hideKeyboard() {
        if(mHideKeyboardCallback!=null) {
            mHideKeyboardCallback.run();
        }
    }


}
