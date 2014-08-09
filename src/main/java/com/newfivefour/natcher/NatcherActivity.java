package com.newfivefour.natcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.natcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
