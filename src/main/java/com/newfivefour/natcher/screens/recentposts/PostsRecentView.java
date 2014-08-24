package com.newfivefour.natcher.screens.recentposts;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.services.PostsRecentService;
import com.newfivefour.natcher.uicomponent.Populatable;
import com.newfivefour.natcher.uicomponent.UiComponentDelegate;
import com.newfivefour.natcher.uicomponent.UiComponentVanilla;
import com.newfivefour.natcher.uicomponent.widgets.LoadingErrorEmptyWidget;
import com.newfivefour.natcher.uicomponent.widgets.SwipeToRefreshWidget;
import com.newfivefour.natcher.uicomponent.widgets.TextViewServerErrorWidget;
import com.newfivefour.natcher.uicomponent.widgets.WindowLoadingSpinnerWidget;

public class PostsRecentView extends FrameLayout implements
        UiComponentDelegate<PostsRecentService.RecentPosts>,
        Populatable<PostsRecentService.RecentPosts> {

    private UiComponentVanilla<PostsRecentService.RecentPosts> mUIComponent;
    private ListView mListView;

    public PostsRecentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View rootView = LayoutInflater.from(context).inflate(R.layout.posts_list_view, this, true);
        mListView = (ListView) rootView.findViewById(R.id.listView);

        // Setup the swipe view
        Activity activity = ((Activity)getContext());
        SwipeToRefreshWidget swipe = new SwipeToRefreshWidget((SwipeRefreshLayout) activity.findViewById(R.id.swipe));

        // Setup ui component
        LoadingErrorEmptyWidget loadingErrorEmptyWidget = new LoadingErrorEmptyWidget(this, -1, R.layout.error_container, R.layout.empty_container);
        mUIComponent = new UiComponentVanilla<>(this);
        mUIComponent
            .setInComponentLoadingDisplay(swipe)
            .setInComponentEmptyDisplay(loadingErrorEmptyWidget)
            .setInComponentServerErrorDisplay(loadingErrorEmptyWidget)
            .setInComponentServerErrorDisplayForUseWhenWeHaveContent(new TextViewServerErrorWidget(getContext().getApplicationContext()))
            .setPageWideLoadingDisplay(new WindowLoadingSpinnerWidget((Activity) getContext()))
            .setRefreshWidget(swipe);
    }

    @SuppressWarnings("unused")
    public PostsRecentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public UiComponentVanilla<PostsRecentService.RecentPosts> getUiComponentDelegate() {
        return mUIComponent;
    }

    @Override
    public void populateWithContent(PostsRecentService.RecentPosts ob) {
        ArrayAdapter<PostsRecentService.RecentPosts.Post> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.posts_list_item,
                ob.getPosts());
        Parcelable listInstance = mListView.onSaveInstanceState();
        mListView.setAdapter(adapter);
        if(listInstance!=null) {
            mListView.onRestoreInstanceState(listInstance);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean shouldShowInComponentLoading() {
        return mListView.getAdapter() == null || mListView.getAdapter().getCount() == 0;
    }

    @Override
    public void clearContent() {
        mListView.setAdapter(null);
    }

}
