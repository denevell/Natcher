package com.newfivefour.natcher.screens.postsrecent;

import com.newfivefour.natcher.app.Application;
import com.newfivefour.natcher.app.Presenter;
import com.newfivefour.natcher.services.PostsRecentService;
import com.squareup.otto.Subscribe;

public class PostsRecentPresenter implements Presenter {

    private final PostsRecentsFragment mView;

    public PostsRecentPresenter(PostsRecentsFragment natcherActivity) {
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
        mView.setRecentPostsError("Network error", error.getResponseCode());
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
