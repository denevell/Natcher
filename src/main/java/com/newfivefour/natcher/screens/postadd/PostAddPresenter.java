package com.newfivefour.natcher.screens.postadd;

import android.os.Bundle;
import android.util.Log;

import com.newfivefour.natcher.app.Application;
import com.newfivefour.natcher.app.Presenter;
import com.newfivefour.natcher.models.PostAdded;
import com.newfivefour.natcher.services.PostAddService;
import com.squareup.otto.Subscribe;

public class PostAddPresenter implements Presenter {

    private static final String TAG = PostAddPresenter.class.getSimpleName();
    private final PostAddFragment mView;
    private final PostAddService mPostAddService;
    private final Bundle mArguments;

    public PostAddPresenter(PostAddFragment activity) {
        mView = activity;
        mArguments = mView.getArguments();
        mPostAddService = new PostAddService(mArguments);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        Application.getEventBus().register(this);
        if(mPostAddService.getNetworkingService().isRequestUnderway(mArguments)) {
            Log.d(TAG, "Add post started from onResume");
            mView.addPostStarted();
        }
    }

    @Override
    public void onPause() {
        Application.getEventBus().unregister(this);
    }

    public void sendPost() {
        mPostAddService.fetch(
                null,
                mView.getPostContent());
        mView.addPostStarted();
    }

    @Subscribe
    public void addPost(PostAdded added) {
        mView.addPostSuccess(added);
    }

    @Subscribe
    public void addPostError(PostAddService.PostAddError error) {
        mView.addPostError(error.responseCode);
    }
}
