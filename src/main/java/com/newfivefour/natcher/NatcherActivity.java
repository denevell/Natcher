package com.newfivefour.natcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;

import com.newfivefour.natcher.screens.postadd.PostAddFragment;
import com.newfivefour.natcher.screens.postsrecent.PostsRecentsFragment;

public class NatcherActivity extends ActionBarActivity implements MainPagePageSwitcher {

    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.natcher_activity);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(mViewPager);
    }

    @Override
    public void gotoRecentPosts() {
        mViewPager.setCurrentItem(0);
        int id = mViewPager.getCurrentItem();
        Fragment f = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager+ ":" + id);
        ((PostsRecentsFragment)f).onResume();
    }

    private void setupViewPager(ViewPager vp) {
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0) {
                    Fragment natcherFragment = new PostsRecentsFragment();
                    natcherFragment.setArguments(new Bundle());
                    return natcherFragment;
                } else {
                    Fragment textFragment = new PostAddFragment();
                    textFragment.setArguments(new Bundle());
                    return textFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        vp.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float v) {
                view.setRotationY(v * 90);
            }
        });
    }
}
