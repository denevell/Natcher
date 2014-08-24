package com.newfivefour.natcher.screens.enterpost;

import com.newfivefour.natcher.app.Application;
import com.newfivefour.natcher.app.Presenter;
import com.newfivefour.natcher.services.PostAddService;
import com.squareup.otto.Subscribe;

public class PostAddPresenter implements Presenter {

    private final PostAddFragment mView;

    public PostAddPresenter(PostAddFragment activity) {
        mView = activity;
    }

    @Override
    public void onResume() {
        Application.getEventBus().register(this);
    }

    @Override
    public void onPause() {
        Application.getEventBus().unregister(this);
    }

    public void sendPost() {
        new PostAddService()
                .fetch(
                        mView.getArguments(),
                        null,
                        mView.getContent());
    }

    @Subscribe
    public void recentPosts(PostAddService.PostAdd addeded) {
    }

    @Subscribe
    public void addPostError(PostAddService.PostAddError error) {
    }
}
