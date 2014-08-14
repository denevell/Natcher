package com.newfivefour.natcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class LoadingWidget {
    private View mLoading;

    public LoadingWidget(ViewGroup view) {
       mLoading = LayoutInflater.from(view.getContext()).inflate(R.layout.loading, view, false);
       if(view instanceof FrameLayout) {
           FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
           mLoading.setLayoutParams(params);
       } else if(view instanceof RelativeLayout) {
           RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
           mLoading.setLayoutParams(params);
       }
       view.addView(mLoading);
    }

    public void hide() {
       mLoading.setVisibility(View.GONE);
    }
}
