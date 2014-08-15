package com.newfivefour.natcher.screens.recentposts;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.newfivefour.natcher.app.component.EmptyableContentConnector;
import com.newfivefour.natcher.app.component.EmptyableComponent;
import com.newfivefour.natcher.app.component.RefreshableComponent;
import com.newfivefour.natcher.app.component.RefreshableContentConnector;
import com.newfivefour.natcher.customviews.LoadingErrorEmptyWidget;
import com.newfivefour.natcher.R;
import com.newfivefour.natcher.app.component.LoadingComponent;
import com.newfivefour.natcher.app.component.ParentLoadingConnector;
import com.newfivefour.natcher.app.component.PopulatableComponent;
import com.newfivefour.natcher.services.PostsRecentService;

public class RecentPostsView extends FrameLayout implements
        PopulatableComponent<PostsRecentService.RecentPosts>,
        ParentLoadingConnector,
        RefreshableContentConnector,
        EmptyableContentConnector {

    private LoadingErrorEmptyWidget mInComponentLoadingErrorWidget;
    private ListView mListView;
    private LoadingComponent mPageWideLoadingConnector;
    private boolean mHasDataFromService;
    private boolean mPageWideLoadingStarted;

    public RecentPostsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.natcher_listview, this, true);
        mInComponentLoadingErrorWidget = new LoadingErrorEmptyWidget(
                this,
                R.layout.loading_layout,
                R.layout.error_container,
                R.layout.empty_container);
        mListView = (ListView) findViewById(R.id.listView);
    }

    public RecentPostsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void populateStarting() {
        mHasDataFromService = false;
        setEmptyError(false);
        setServerError(false);
        if(isEmptyContent()) {
            setLoading(true);
        } else {
            setPageWideLoading(true);
        }
    }

    @Override
    public void populateFromCache(PostsRecentService.RecentPosts ob) {
        populateListView(ob);
        setLoading(false);
        setServerError(false);
        setEmptyError(false);
        if(!mHasDataFromService) {
            setPageWideLoading(true);
        }
    }

    @Override
    public void populateFromServer(PostsRecentService.RecentPosts ob) {
        populateListView(ob);
        setPageWideLoading(false);
        setLoading(false);
        setServerError(false);
        setEmptyError(false);
    }

    @Override
    public void populateFromServerError() {
        if(isEmptyContent()) {
            setServerError(true);
        } else {
            setServerError(false);
        }
        setPageWideLoading(false);
        setLoading(false);
        setEmptyError(false);
    }

    @Override
    public void populateWithEmptyContentFromServer() {
        clearListView();
        setPageWideLoading(false);
        setLoading(false);
        setServerError(false);
        setEmptyError(true);
    }

    @Override
    public void populateWithEmptyContentFromCache() {
        clearListView();
        setLoading(false);
        setServerError(false);
        setEmptyError(true);
    }

    @Override
    public void setPageWideLoadingConnector(LoadingComponent loadingComponent) {
        mPageWideLoadingConnector = loadingComponent;
    }

    @Override
    public void setRefreshConnector(RefreshableComponent connector) {
        if(mInComponentLoadingErrorWidget!=null) {
            mInComponentLoadingErrorWidget.setRefreshConnector(connector);
        }
    }

    @Override
    public void setEmptyConnector(EmptyableComponent connector) {
        if(mInComponentLoadingErrorWidget!=null) {
            mInComponentLoadingErrorWidget.setEmptyConnector(connector);
        }
    }

    private void populateListView(PostsRecentService.RecentPosts ob) {
        ArrayAdapter<PostsRecentService.RecentPosts.Post> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.post_list_item,
                ob.getPosts());
        Parcelable listInstance = mListView.onSaveInstanceState();
        mListView.setAdapter(adapter);
        if(listInstance!=null) {
            mListView.onRestoreInstanceState(listInstance);
        }
        adapter.notifyDataSetChanged();
    }

    public boolean isEmptyContent() {
        return mListView.getAdapter() == null || mListView.getAdapter().getCount() == 0;
    }

    private void clearListView() {
        mListView.setAdapter(null);
    }

    private void setPageWideLoading(boolean show) {
        if(mPageWideLoadingConnector !=null) {
            if(show && mPageWideLoadingStarted) {
                return;
            }
            if(!show && !mPageWideLoadingStarted) {
                return;
            }
            mPageWideLoadingStarted = show;
            mPageWideLoadingConnector.loadingStart(show);
        }
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
}
