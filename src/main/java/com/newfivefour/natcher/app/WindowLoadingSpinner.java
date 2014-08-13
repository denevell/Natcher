package com.newfivefour.natcher.app;

import android.app.Activity;

import com.newfivefour.natcher.app.component.LoadingComponent;

public class WindowLoadingSpinner implements LoadingComponent {

    private final Activity mActivity;

    public WindowLoadingSpinner(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void loadingStop() {
        if(mActivity!=null) {
            mActivity.setProgressBarIndeterminateVisibility(false);
            mActivity.setProgressBarVisibility(false);
        }
    }

    @Override
    public void loadingStart() {
        if(mActivity!=null) {
            mActivity.setProgressBarIndeterminateVisibility(true);
            mActivity.setProgressBarVisibility(true);
        }
    }
}
