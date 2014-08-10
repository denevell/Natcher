package com.newfivefour.natcher;

import com.newfivefour.natcher.app.Application;
import com.newfivefour.natcher.app.Presenter;
import com.newfivefour.natcher.services.PostsRecentService;
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
        new PostsRecentService().fetch(mView.getArguments());
    }

    @Override
    public void onPause() {
        Application.getEventBus().unregister(this);
    }

    @Subscribe
    public void recentPosts(PostsRecentService.RecentPosts posts) {
        mView.startLoading(false);
        mView.setPosts(posts);
    }

    @Subscribe
    public void recentPostsCached(PostsRecentService.RecentPostsCached cached) {
        mView.setPosts(cached.returnCached());
    }

    @Subscribe
    public void recentPostsError(PostsRecentService.RecentPostsError error) {
        mView.startLoading(false);
        mView.setError("Network error");
    }
}
