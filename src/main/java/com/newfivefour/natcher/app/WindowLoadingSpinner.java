package com.newfivefour.natcher.app;

import android.app.Activity;
import android.util.Log;

import com.newfivefour.natcher.app.component.LoadingComponent;

public class WindowLoadingSpinner implements LoadingComponent {

    private static final String TAG = WindowLoadingSpinner.class.getSimpleName();
    private final Activity mActivity;

    public WindowLoadingSpinner(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void loadingStart(boolean start) {
        if(mActivity==null) return;
        if(start) {
            Log.d(TAG, "Calling loading start");
            mActivity.setProgressBarIndeterminateVisibility(true);
            mActivity.setProgressBarVisibility(true);
        } else {
            Log.d(TAG, "Calling loading stop");
            mActivity.setProgressBarIndeterminateVisibility(false);
            mActivity.setProgressBarVisibility(false);
        }
    }
}
