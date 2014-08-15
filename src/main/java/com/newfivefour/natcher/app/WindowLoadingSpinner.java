package com.newfivefour.natcher.app;

import android.app.Activity;

import com.newfivefour.natcher.app.component.LoadingComponent;

public class WindowLoadingSpinner implements LoadingComponent {

    private final Activity mActivity;

    public WindowLoadingSpinner(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void loadingStart(boolean start) {
        if(mActivity!=null && start) {
            mActivity.setProgressBarIndeterminateVisibility(true);
            mActivity.setProgressBarVisibility(true);
        } else {
            mActivity.setProgressBarIndeterminateVisibility(false);
            mActivity.setProgressBarVisibility(false);
        }
    }
}
