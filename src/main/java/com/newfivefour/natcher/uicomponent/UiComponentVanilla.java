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
 * Then do nothing (the parent should show the error)
 *
 * # Given we have received new server content
 * Then hide the server error
 *
 * # Given we have received cached content
 * Then hide the server error
 *
 * # Given we have received empty content from server
 * Then hide the server error
 *
 * # Given we have received empty content from cache
 * Then hide the server error
 *
 * # Given a loading start call
 * Then hide the server error
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
 * ### QUESTION: Show fragment server error along with component server error?
 * ### ANSWER:   At the moment, the fragment doesn't know if the component will
 * ###           show an error on networking problem, so it does so anyway. The
 * ###           component won't  show one on cached content, but will on no content.
 * ###           To be thought about. Maybe I need an error on cached content?
 *
 *
 */
public class UiComponentVanilla<T> implements UiComponent<T> {
    private static String TAG = UiComponentVanilla.class.getSimpleName();
    private OnRefreshConnector mRefreshWidget;

    private LoadingView mPageWideLoader;
    private LoadingView mInComponentLoading;
    private EmptyView mEmptyViewView;
    private ServerErrorView mServerErrorComponentView;

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
        mEmptyViewView = emptyView;
        return this;
    }

    public UiComponentVanilla<T> setInComponentServerErrorDisplay(ServerErrorView errorComponent) {
        mServerErrorComponentView = errorComponent;
        return this;
    }

    /**
     * @param refreshWidget This will be given the OnRefresh callback when it's set on the UiComponent
     * @return
     */
    public UiComponentVanilla<T> setRefreshWidget(OnRefreshConnector refreshWidget) {
        mRefreshWidget = refreshWidget;
        return this;
    }

    @Override
    public void setEmptyConnector(OnEmpty connector) {
        Log.d(TAG, "setEmptyConnector()");
        if(mEmptyViewView !=null) {
            mEmptyViewView.setEmptyConnector(connector);
        }
    }

    @Override
    public void setRefreshableConnector(OnRefresh connector) {
        Log.d(TAG, "setRefreshableConnector()");
        if(mEmptyViewView !=null) {
            mEmptyViewView.setRefreshableConnector(connector);
        }
        if(mServerErrorComponentView!=null) {
            mServerErrorComponentView.setRefreshableConnector(connector);
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
        setServerError(false);
        if(mPopulatable.isContentEmpty()) {
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
        mPopulatable.populateWithContent(ob);
        setLoading(false);
        setServerError(false);
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
        mPopulatable.populateWithContent(ob);
        setPageWideLoading(false);
        setLoading(false);
        setServerError(false);
        setEmptyError(false);
    }

    @Override
    public void populateFromServerError(int responseCode) {
        Log.d(TAG, "populateFromServerError()");
        mShouldStartPageLoaderAfterCachedResult = false;
        if(mPopulatable.isContentEmpty()) {
            Log.d(TAG, "populateFromServerError(): empty view content");
            setServerError(true);
        } else {
            Log.d(TAG, "populateFromServerError(): non empty view content");
            setServerError(false);
        }
        setPageWideLoading(false);
        setLoading(false);
        setEmptyError(false);
    }

    @Override
    public void populateWithEmptyContentFromServer() {
        Log.d(TAG, "populateWithEmptyContentFromServer()");
        mPopulatable.clearContent();
        setPageWideLoading(false);
        setLoading(false);
        setServerError(false);
        setEmptyError(true);
    }

    @Override
    public void populateWithEmptyContentFromCache() {
        Log.d(TAG, "populateWithEmptyContentFromCache()");
        mPopulatable.clearContent();
        setLoading(false);
        setServerError(false);
        setEmptyError(true);
    }

    private void setLoading(boolean show) {
        if(mInComponentLoading!=null) {
            mInComponentLoading.showLoading(show);
        }
    }

    private void setServerError(boolean show) {
        if(mServerErrorComponentView!=null) {
            mServerErrorComponentView.showServerError(show);
        }
    }

    private void setEmptyError(boolean show) {
        if(mEmptyViewView !=null) {
            mEmptyViewView.showEmpty(show);
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
