package com.newfivefour.natcher.uicomponent;

import android.util.Log;
import android.view.ViewGroup;

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
 * ### QUESTION: Are we leaking objects?
 *
 * ### QUESTION: Show fragment server error along with component server error?
 * ### ANSWER:   At the moment, the fragment doesn't know if the component will
 * ###           show an error on networking problem, so it does so anyway. The
 * ###           component won't  show one on cached content, but will on no content.
 * ###           To be thought about.
 *
 * ### QUESTION: Calling the populateStarting before getting a response from the server?
 * ### ANSWER:   To be thought about, the page wide boolean and shouldStartPagerLoaderAfterCache.
 * ###           shouldStartPageL... may be problem if it's set to false, and then true by
 * ###           a repeat call, but there'll only be once server response, since the networking
 * ###           code doesn't do repeat requests if the first is already under way. Will see if we do pull to request,
 * ###           meaning we may need to ensure populateStarting isn't called twice for the same request.
 *
 * ### QUESTION: What if we want to refresh the component when it already has data?
 * ### ANSWER:   We need something like pull to refresh or swipe to refresh.
 *
 * ### QUESTION: What about occasions when you don't want to get the data again on rotation etc, only when the user says?
 * ### ANSWER:   In this case, we'll have the page-wide loading spinner keep displaying, since they only are removed
 * ###           when the server returns a result, an error or empty content.
 * ###           We could get around this, outside the component, by simply setting the overall page loader to false
 * ###           after setting the cached content
 * ###           If we were going to do it with the networking message bus service, then we'd need a way to say only give
 * ###           me the cached content. We could alternatively use the response cache object directly.
 */
public class UiComponentVanilla<T> implements UiComponent<T> {
    private static String TAG = UiComponentVanilla.class.getSimpleName();
    private LoadingErrorEmptyWidget mInComponentLoadingErrorWidget;
    private LoadingComponent mPageWideLoader;

    /**
     * Used to communicate with whatever is using this as a delegate
     */
    private final Populatable<T> mPopulatable;

    /**
     * We don't need to store this to be given to this again on creation if we're giving this again on creation as default false,
     * and later true when loading starts.
     *
     * And we will also get that again, since our requests will fire again, thereby calling start loading.
     *
     * This isn't a problem, per se, since our networking implementation won't be sending out the same requests again,
     * just finishing existing ones.
     */
    private boolean mPageWideLoadingStarted;

    /**
     * We, as above, don't need to store this to be given to this again on creation.
     *
     * This is because the true state will be here by default, and you'll want that
     * on a creation, since you'll be getting the data again.
     */
    private boolean mShouldStartPageLoaderAfterCachedResult = true;

    /**
     * @param populatable How we populate the view on server response, detect if it's empty and clear it if needs be
     * @param parent The view the loading, error and empty layouts are going to attach to
     */
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
    public void populateStarting() {
        Log.d(TAG, "populateStarting()");
        mShouldStartPageLoaderAfterCachedResult = true;
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
        if(mShouldStartPageLoaderAfterCachedResult) {
            Log.d(TAG, "populateFromCache(): setPageWideLoading");
            setPageWideLoading(true);
        }
    }

    @Override
    public void populateFromServer(T ob) {
        Log.d(TAG, "populateFromServer()");
        mShouldStartPageLoaderAfterCachedResult = false;
        mPopulatable.populate(ob);
        setPageWideLoading(false);
        setLoading(false);
        setServerError(false);
        setEmptyError(false);
    }

    @Override
    public void populateFromServerError() {
        Log.d(TAG, "populateFromServerError()");
        mShouldStartPageLoaderAfterCachedResult = false;
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
