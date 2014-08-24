package com.newfivefour.natcher.screens.enterpost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.newfivefour.natcher.R;


public class PostAddFragment extends android.app.Fragment {

    private PostAddPresenter mPresenter;
    private PostAddView mAddPost;

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

    public String getContent() {
        return mAddPost.getPostContent();
    }

}
