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
 */
public class UiComponentVanilla<T> implements UiComponent<T> {
    private static String TAG = UiComponentVanilla.class.getSimpleName();
    private OnRefreshConnector mRefreshWidget;

    private LoadingView mPageWideLoader;
    private LoadingView mInComponentLoading;
    private EmptyView mEmptyView;
    private ServerErrorView mServerErrorView;
    private ServerErrorView mServerErrorViewWhenWeHaveContent;

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

    public UiComponentVanilla<T> setInComponentServerErrorDisplay(ServerErrorView errorComponent) {
        mServerErrorView = errorComponent;
        return this;
    }

    public UiComponentVanilla<T> setInComponentServerErrorDisplayForUseWhenWeHaveContent(ServerErrorView errorComponent) {
        mServerErrorViewWhenWeHaveContent = errorComponent;
        return this;
    }

    /**
     * @param refreshWidget This will be given the OnRefresh callback when it's set on the UiComponent
     */
    public UiComponentVanilla<T> setRefreshWidget(OnRefreshConnector refreshWidget) {
        mRefreshWidget = refreshWidget;
        return this;
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

    @Override
    public UiComponentVanilla<T> setPageWideLoadingDisplay(LoadingView loadingView) {
        Log.d(TAG, "setPageWideLoadingDisplay()");
        mPageWideLoader = loadingView;
        return this;
    }

    @Override
    public void onResetComponent() {
        // This will only do so if it's active
        setPageWideLoading(false);
        // Not really needed, since we're resetting the component, but may be useful if
        // the loading is outside the component (naughty)
        setLoading(false);
    }

    @Override
    public void populateStarting() {
        Log.d(TAG, "populateStarting()");
        mShouldStartPageLoaderAfterCachedResult = true;
        setEmptyError(false);
        hideServerErrors();
        if(mPopulatable.shouldShowInComponentLoading()) {
            Log.d(TAG, "populateStarting(): set in-component loading");
            setLoading(true);
        } else {
            Log.d(TAG, "populateStarting(): set page-wide loading");
            setPageWideLoading(true);
        }
    }

    @Override
    public void populateFromCache(T ob) {
        Log.d(TAG, "populateFromCache()");
        mPopulatable.populateOnSuccessfulResponse(ob);
        setLoading(false);
        hideServerErrors();
        setEmptyError(false);
        if(mShouldStartPageLoaderAfterCachedResult) {
            Log.d(TAG, "populateFromCache(): setPageWideLoading");
            setPageWideLoading(true);
        }
    }

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

    @Override
    public void populateFromServerError(int responseCode) {
        Log.d(TAG, "populateFromServerError()");
        mShouldStartPageLoaderAfterCachedResult = false;
        if(mPopulatable.shouldShowInComponentLoading()) {
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

    @Override
    public void populateWithEmptyContentFromServer() {
        Log.d(TAG, "populateWithEmptyContentFromServer()");
        mPopulatable.clearContentWhenServerReturnsEmptyResponse();
        setPageWideLoading(false);
        setLoading(false);
        hideServerErrors();
        setEmptyError(true);
    }

    @Override
    public void populateWithEmptyContentFromCache() {
        Log.d(TAG, "populateWithEmptyContentFromCache()");
        mPopulatable.clearContentWhenServerReturnsEmptyResponse();
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

}
