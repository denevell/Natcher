package com.newfivefour.natcher;

import com.newfivefour.natcher.app.Application;
import com.newfivefour.natcher.app.Presenter;
import com.newfivefour.natcher.networking.RecentPostsService;
import com.squareup.otto.Subscribe;

public class NatcherPresenter implements Presenter {

    private final NatcherFragment mView;

    public NatcherPresenter(NatcherFragment natcherActivity) {
        mView = natcherActivity;
    }

    @Override
    public void onResume() {
        Application.getEventBus().register(this);
        mView.startLoading(true);
        new RecentPostsService().fetch(mView);
    }

    @Override
    public void onPause() {
        Application.getEventBus().unregister(this);
    }

    @Subscribe
    public void recentPosts(RecentPostsService.RecentPosts posts) {
        mView.startLoading(false);
        mView.setText(posts.getPosts().get(0).getContent());
    }

    @Subscribe
    public void recentPostsCached(RecentPostsService.RecentPostsCached cached) {
        mView.setText("Cached: " + cached.returnCached().getPosts().get(0).getContent());
    }

    @Subscribe
    public void recentPostsError(RecentPostsService.RecentPostsError error) {
        mView.startLoading(false);
        mView.setError("Network error");
    }
}
