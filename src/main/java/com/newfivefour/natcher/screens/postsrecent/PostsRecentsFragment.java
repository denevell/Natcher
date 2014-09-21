package com.newfivefour.natcher.screens.postsrecent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.services.PostsRecentService;
import com.newfivefour.natcher.uicomponent.events.OnEmptyCallback;
import com.newfivefour.natcher.uicomponent.events.OnRefreshCallback;
import com.newfivefour.natcher.uicomponent.widgets.WindowLoadingSpinnerWidget;

public class PostsRecentsFragment extends Fragment {

    private static final String TAG = PostsRecentsFragment.class.getSimpleName();
    private PostsRecentPresenter mPresenter;
    private PostsRecentView mPostsRecentView;
    private WindowLoadingSpinnerWidget mWindowLoadingSpinnerDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PostsRecentPresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.posts_list_fragment, container, false);
        mPostsRecentView = (PostsRecentView) v.findViewById(R.id.natcher_listview);

        mPostsRecentView.getUiComponentDelegate().setRefreshableCallback(new OnRefreshCallback() {
            @Override
            public void onRefreshContent() {
                Log.d(TAG, "onRefreshContent()");
                mPresenter.recentPostsFetch();
            }
        });
        mPostsRecentView.getUiComponentDelegate().setEmptyCallback(new OnEmptyCallback() {
            @Override
            public void onIsEmpty() {
                Toast.makeText(getActivity(), "Yeah, it's empty.", Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
        mPostsRecentView.getUiComponentDelegate().onResetComponent();
    }

    public void setRecentPostsLoadingStart() {
        mPostsRecentView.getUiComponentDelegate().populateStarting();
    }

    public void setRecentPosts(PostsRecentService.RecentPosts recentPosts) {
        mPostsRecentView.getUiComponentDelegate().populateFromServer(recentPosts);
    }

    public void setRecentPostsFromCache(PostsRecentService.RecentPosts recentPosts) {
        mPostsRecentView.getUiComponentDelegate().populateFromCache(recentPosts);
    }

    public void setRecentPostsEmptyFromCache() {
        mPostsRecentView.getUiComponentDelegate().populateWithEmptyContentFromCache();
    }

    public void setRecentPostsEmptyFromServer() {
        mPostsRecentView.getUiComponentDelegate().populateWithEmptyContentFromServer();
    }

    public void setRecentPostsError(String s, int responseCode) {
        mPostsRecentView.getUiComponentDelegate().populateFromServerError(responseCode, s);
    }

}
