package com.newfivefour.natcher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.newfivefour.natcher.networking.RecentPostsService;
import com.squareup.otto.Subscribe;


public class NatcherActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.natcher_activity);
        mTextView = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application.getEventBus().register(this);
        new RecentPostsService().fetch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Application.getEventBus().unregister(this);
    }

    @Subscribe
    public void recentPosts(RecentPostsService.RecentPosts posts) {
        mTextView.setText(posts.getPosts().get(0).getContent());
    }

    @Subscribe
    public void recentPostsCached(RecentPostsService.RecentPostsCached cached) {
        mTextView.setText("Cached: " + cached.returnCached().getPosts().get(0).getContent());
    }

    @Subscribe
    public void recentPostsError(RecentPostsService.RecentPostsError error) {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }
}
