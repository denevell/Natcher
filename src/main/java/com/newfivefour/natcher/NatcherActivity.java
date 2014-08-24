package com.newfivefour.natcher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.newfivefour.natcher.app.swiping.SwipeInterface;
import com.newfivefour.natcher.screens.postadd.PostAddFragment;
import com.newfivefour.natcher.screens.postsrecent.PostsRecentsFragment;


public class NatcherActivity extends FragmentActivity implements SwipeInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.natcher_activity);
        ViewPager vp = (ViewPager) findViewById(R.id.view_pager);
        vp.setAdapter(new android.support.v13.app.FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(i==0) {
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
            @Override public void transformPage(View view, float v) {
               view.setRotationY(v * 180);
            }
        });
    }

    @Override
    public void bottom2top(View v) {

    }

    @Override
    public void left2right(View v) {

    }

    @Override
    public void right2left(View v) {

    }

    @Override
    public void top2bottom(View v) {

    }
}
