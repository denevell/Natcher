package com.newfivefour.natcher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.newfivefour.natcher.app.swiping.SwipeInterface;
import com.newfivefour.natcher.screens.enterpost.TextFragment;
import com.newfivefour.natcher.screens.recentposts.NatcherFragment;


public class NatcherActivity extends FragmentActivity implements SwipeInterface {

    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.natcher_activity);

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container);
                if(currentFragment instanceof NatcherFragment) {
                    gotoOrangeFragment();
                } else {
                   gotoNatcherFragment();
                }
            }
        });

        if(savedInstanceState==null) {
            gotoNatcherFragment();
        }
    }

    private void gotoOrangeFragment() {
        Fragment textFragment = getFragmentManager().findFragmentByTag(TextFragment.class.getSimpleName());
        if(textFragment==null) {
            textFragment = new TextFragment();
        }
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .addToBackStack(TextFragment.class.getSimpleName())
                .replace(R.id.container, textFragment, TextFragment.class.getSimpleName())
                .commit();
    }

    private void gotoNatcherFragment() {
        Fragment natcherFragment = getFragmentManager().findFragmentByTag(NatcherFragment.class.getSimpleName());
        if(natcherFragment==null) {
            natcherFragment = new NatcherFragment();
        }
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out,
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .addToBackStack(NatcherFragment.class.getSimpleName())
                .replace(R.id.container, natcherFragment, NatcherFragment.class.getSimpleName())
                .commit();
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
