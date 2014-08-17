package com.newfivefour.natcher.screens.recentposts;

import android.content.Context;
import android.os.Parcelable;
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

public class RecentPostsView extends FrameLayout implements
        UiComponentDelegate<PostsRecentService.RecentPosts>,
        Populatable<PostsRecentService.RecentPosts> {

    private UiComponentVanilla<PostsRecentService.RecentPosts> mUIComponent;
    private ListView mListView;

    public RecentPostsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View rootView = LayoutInflater.from(context).inflate(R.layout.natcher_listview, this, true);
        mListView = (ListView) rootView.findViewById(R.id.listView);

        // Setup ui component
        mUIComponent = new UiComponentVanilla<PostsRecentService.RecentPosts>()
            .setConnector(this)
            .setErrorEmptyLoading(
                this,
                R.layout.loading_layout,
                R.layout.error_container,
                R.layout.empty_container);
    }

    @SuppressWarnings("unused")
    public RecentPostsView(Context context, AttributeSet attrs) {
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
    public boolean isContentEmpty() {
        return mListView.getAdapter() == null || mListView.getAdapter().getCount() == 0;
    }

    @Override
    public void clearContent() {
        mListView.setAdapter(null);
    }

}
