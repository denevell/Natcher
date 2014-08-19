package com.newfivefour.natcher.screens.recentposts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.services.PostsRecentService;
import com.newfivefour.natcher.uicomponent.events.OnEmpty;
import com.newfivefour.natcher.uicomponent.events.OnRefresh;
import com.newfivefour.natcher.uicomponent.widgets.WindowLoadingSpinnerWidget;

public class NatcherFragment extends android.app.Fragment {

    private static final String TAG = NatcherFragment.class.getSimpleName();
    private NatcherPresenter mPresenter;
    private RecentPostsView mRecentPostsView;
    private WindowLoadingSpinnerWidget mWindowLoadingSpinnerDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new NatcherPresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.natcher_fragment, container, false);
        mRecentPostsView = (RecentPostsView) v.findViewById(R.id.natcher_listview);

        mRecentPostsView.getUiComponentDelegate().setRefreshableConnector(new OnRefresh() {
            @Override
            public void onRefreshContent() {
                Log.d(TAG, "onRefreshContent()");
                mPresenter.recentPostsFetch();
            }
        });
        mRecentPostsView.getUiComponentDelegate().setEmptyConnector(new OnEmpty() {
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
        mRecentPostsView.getUiComponentDelegate().onResetComponent();
    }

    public void setRecentPostsLoadingStart() {
        mRecentPostsView.getUiComponentDelegate().populateStarting();
    }

    public void setRecentPosts(PostsRecentService.RecentPosts recentPosts) {
        mRecentPostsView.getUiComponentDelegate().populateFromServer(recentPosts);
    }

    public void setRecentPostsFromCache(PostsRecentService.RecentPosts recentPosts) {
        mRecentPostsView.getUiComponentDelegate().populateFromCache(recentPosts);
    }

    public void setRecentPostsEmptyFromCache() {
        mRecentPostsView.getUiComponentDelegate().populateWithEmptyContentFromCache();
    }

    public void setRecentPostsEmptyFromServer() {
        mRecentPostsView.getUiComponentDelegate().populateWithEmptyContentFromServer();
    }

    public void setRecentPostsError(String s, int responseCode) {
        mRecentPostsView.getUiComponentDelegate().populateFromServerError(responseCode);
    }

}
