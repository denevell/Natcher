package com.newfivefour.natcher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


public class NatcherActivity extends FragmentActivity {

    private TextView mTextView;
    private Fragment natcherFragment = new NatcherFragment();
    private Fragment textFragment = new TextFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.natcher_activity);

        natcherFragment.setArguments(new Bundle());

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
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .addToBackStack(TextFragment.class.getSimpleName())
                .replace(R.id.container, textFragment)
                .commit();
    }

    private void gotoNatcherFragment() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out,
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .addToBackStack(NatcherFragment.class.getSimpleName())
                .replace(R.id.container, natcherFragment)
                .commit();
    }
}
