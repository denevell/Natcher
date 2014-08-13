package com.newfivefour.natcher.screens.recentposts;

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
        new PostsRecentService().fetch(mView.getArguments(), true);
    }

    @Override
    public void onPause() {
        Application.getEventBus().unregister(this);
    }

    @Subscribe
    public void recentPosts(PostsRecentService.RecentPosts posts) {
        mView.setRecentPosts(posts);
    }

    @Subscribe
    public void recentPostsCached(PostsRecentService.RecentPostsCached cached) {
        mView.setRecentPostsFromCache(cached.returnCached());
    }

    @Subscribe
    public void recentPostsError(PostsRecentService.RecentPostsError error) {
        mView.setRecentPostsError("Network error");
    }
}
