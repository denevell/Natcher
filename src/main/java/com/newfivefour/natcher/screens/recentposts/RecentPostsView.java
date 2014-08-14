package com.newfivefour.natcher.screens.recentposts;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.newfivefour.natcher.LoadingWidget;
import com.newfivefour.natcher.R;
import com.newfivefour.natcher.app.component.LoadingComponent;
import com.newfivefour.natcher.app.component.ParentLoadingConnector;
import com.newfivefour.natcher.app.component.PopulatableComponent;
import com.newfivefour.natcher.services.PostsRecentService;

public class RecentPostsView extends FrameLayout implements
        PopulatableComponent<PostsRecentService.RecentPosts>,
        ParentLoadingConnector {

    private final LoadingWidget mInComponentLoadingWidget;
    private ListView mListView;
    private LoadingComponent mPageWideLoadingConnector;
    private boolean mHasSetFromServer;

    public RecentPostsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.natcher_listview, this, true);
        mInComponentLoadingWidget = new LoadingWidget(this);
        mListView = (ListView) findViewById(R.id.listView);
    }

    public RecentPostsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void populateFromCache(PostsRecentService.RecentPosts ob) {
        populateListView(ob);
        setDisabledInComponentLoading();
        if(!mHasSetFromServer && mPageWideLoadingConnector !=null) {
            mPageWideLoadingConnector.loadingStart();
        }
    }

    @Override
    public void populateFromServer(PostsRecentService.RecentPosts ob) {
        populateListView(ob);
        setDisabledPageWideLoading();
        setDisabledInComponentLoading();
        mHasSetFromServer = true;
    }

    @Override
    public void populateFromServerError() {
        setDisabledPageWideLoading();
        setDisabledInComponentLoading();
    }

    @Override
    public void isEmpty() {
        setDisabledPageWideLoading();
        setDisabledInComponentLoading();
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

    @Override
    public void setPageWideLoadingConnector(LoadingComponent loadingComponent) {
        mPageWideLoadingConnector = loadingComponent;
    }

    private void setDisabledPageWideLoading() {
        if(mPageWideLoadingConnector !=null) {
            mPageWideLoadingConnector.loadingStop();
        }
    }

    private void setDisabledInComponentLoading() {
        if(mInComponentLoadingWidget!=null) {
            mInComponentLoadingWidget.hide();
        }
    }
}
