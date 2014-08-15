package com.newfivefour.natcher.app.component;

import android.util.Log;
import android.view.ViewGroup;

import com.newfivefour.natcher.customviews.LoadingErrorEmptyWidget;

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
 * # Given populate loading is called
 * Given the component's content is there
 * Then the overall page loading display should be set active
 *
 * # Given cached content is returned
 * Given a service hasn't returned data
 * Then show the overall page loader
 *
 * # Given the server returns good content
 * Then the overall page loading display should be removed
 *
 * # Given the component has received a server error
 * Then the overall page loading display should be removed
 *
 * # Given we have received empty content from server
 * Then the overall page loading display should be removed
 *
 * # Given we have received empty content from cache
 * Then the overall page loading display should NOT be removed
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
 * ### FIX: Keeping state on rotation
 * ### QUESTION: Show fragment server error along with component server error?
 * ### QUESTION: Calling the populateStarting before first one can get to populated by server, server error, or is empty
 * ### QUESTION: What if we want to refresh to component when it already has data?
 */
public class UiComponentVanilla<T> implements UiComponent<T> {
    private final Populatable<T> mPopulatable;
    private LoadingErrorEmptyWidget mInComponentLoadingErrorWidget;
    private LoadingComponent mPageWideLoader;
    private boolean mPageWideLoadingStarted;
    private boolean mHasDataFromService;
    private static String TAG = UiComponentVanilla.class.getSimpleName();

    public UiComponentVanilla(
            Populatable<T> populatable,
            ViewGroup parent,
            int loadingLayout,
            int errorLayout,
            int emptyLayout) {
        mInComponentLoadingErrorWidget = new LoadingErrorEmptyWidget(
                parent,
                loadingLayout,
                errorLayout,
                emptyLayout
                );
        mPopulatable = populatable;
    }

    @Override
    public void setEmptyConnector(EmptiableComponent connector) {
        Log.d(TAG, "setEmptyConnector()");
        if(mInComponentLoadingErrorWidget!=null) {
            mInComponentLoadingErrorWidget.setEmptyConnector(connector);
        }
    }

    @Override
    public void setRefreshConnector(RefreshableComponent connector) {
        Log.d(TAG, "setRefreshConnector()");
        if(mInComponentLoadingErrorWidget!=null) {
            mInComponentLoadingErrorWidget.setRefreshConnector(connector);
        }
    }

    @Override
    public void setPageWideLoadingConnector(LoadingComponent loadingComponent) {
        Log.d(TAG, "setPageWideLoadingConnector()");
        mPageWideLoader = loadingComponent;
    }

    @Override
    public void populateStarting() {
        Log.d(TAG, "populateStarting()");
        mHasDataFromService = false;
        setEmptyError(false);
        setServerError(false);
        if(mPopulatable.hasEmptyContent()) {
            mInComponentLoadingErrorWidget.showLoading(true);
        } else {
            setPageWideLoading(true);
        }
    }

    @Override
    public void populateFromCache(T ob) {
        Log.d(TAG, "populateFromCache()");
        mPopulatable.populate(ob);
        setLoading(false);
        setServerError(false);
        setEmptyError(false);
        if(!mHasDataFromService) {
            Log.d(TAG, "populateFromCache(): setPageWideLoading");
            setPageWideLoading(true);
        }
    }

    @Override
    public void populateFromServer(T ob) {
        Log.d(TAG, "populateFromServer()");
        mHasDataFromService = true;
        mPopulatable.populate(ob);
        setPageWideLoading(false);
        setLoading(false);
        setServerError(false);
        setEmptyError(false);
    }

    @Override
    public void populateFromServerError() {
        Log.d(TAG, "populateFromServerError()");
        mHasDataFromService = true;
        if(mPopulatable.hasEmptyContent()) {
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
        mPopulatable.clearExistingContent();
        setPageWideLoading(false);
        setLoading(false);
        setServerError(false);
        setEmptyError(true);
    }

    @Override
    public void populateWithEmptyContentFromCache() {
        Log.d(TAG, "populateWithEmptyContentFromCache()");
        mPopulatable.clearExistingContent();
        setLoading(false);
        setServerError(false);
        setEmptyError(true);
    }

    private void setLoading(boolean show) {
        if(mInComponentLoadingErrorWidget !=null) {
            mInComponentLoadingErrorWidget.showLoading(show);
        }
    }

    private void setServerError(boolean show) {
        if(mInComponentLoadingErrorWidget!=null) {
            mInComponentLoadingErrorWidget.showError(show);
        }
    }

    private void setEmptyError(boolean show) {
        if(mInComponentLoadingErrorWidget!=null) {
            mInComponentLoadingErrorWidget.showEmpty(show);
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
            mPageWideLoader.loadingStart(show);
        }
    }
}
