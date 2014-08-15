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
        recentPostsFetch();
    }

    @Override
    public void onPause() {
        Application.getEventBus().unregister(this);
    }

    public void recentPostsFetch() {
        new PostsRecentService().fetch(mView.getArguments());
        mView.setRecentPostsLoadingStart();
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

    @Subscribe
    public void recentPostsEmpty (PostsRecentService.RecentPostsEmpty empty) {
        if(empty.isFromCached()) {
            mView.setRecentPostsEmptyFromCache();
        } else {
            mView.setRecentPostsEmptyFromServer();
        }
    }
}
