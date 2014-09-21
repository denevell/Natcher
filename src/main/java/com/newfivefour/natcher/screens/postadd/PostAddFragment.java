package com.newfivefour.natcher.screens.postadd;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.newfivefour.natcher.MainPagePageSwitcher;
import com.newfivefour.natcher.R;
import com.newfivefour.natcher.models.PostAdded;


public class PostAddFragment extends Fragment {

    private static final String TAG = PostAddFragment.class.getSimpleName();
    private PostAddPresenter mPresenter;
    private PostAddView mAddPost;
    private MainPagePageSwitcher mMainPageSwitcher;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof MainPagePageSwitcher)) {
            throw new RuntimeException("Parent needs to implement correct interface");
        }
        mMainPageSwitcher = (MainPagePageSwitcher) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PostAddPresenter(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.post_add_fragment, container, false);
        mAddPost = (PostAddView) v.findViewById(R.id.add_post_view);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.addpost, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.sendPost();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    public void addPostStarted() {
        Log.d(TAG, "addPostStarted");
        mAddPost.getUiComponentDelegate().populateStarting();
    }

    public void addPostError(int errorCode, String message) {
        mAddPost.getUiComponentDelegate().populateFromServerError(errorCode, message);
    }

    public void addPostSuccess(PostAdded postAdd) {
        mAddPost.getUiComponentDelegate().populateFromServer(postAdd);
        mMainPageSwitcher.gotoRecentPosts();
    }

    public String getPostContent() {
        return mAddPost.getPostContent();
    }

    public String getPostSubject() {
        return mAddPost.getPostSubject();
    }

}
