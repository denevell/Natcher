package com.newfivefour.natcher.uicomponent.widgets;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.newfivefour.natcher.uicomponent.views.LoadingDisplay;

/**
 * Can be used if we're using something other than the swipe to refresh pattern for loading.
 */
public class WindowLoadingSpinnerWidget implements LoadingDisplay {

    private static int count = 0;
    private static final String TAG = WindowLoadingSpinnerWidget.class.getSimpleName();
    private final ActionBarActivity mActivity;

    public WindowLoadingSpinnerWidget(ActionBarActivity activity) {
        mActivity = activity;
    }

    @Override
    public void showLoading(boolean start) {
        if(start) {
            count++;
        } else {
            count--;
        }
        Log.d(TAG, "Loading count is: " + count);
        if(mActivity==null) return;
        if(start) {
            Log.d(TAG, "Calling loading start");
            mActivity.setSupportProgressBarIndeterminateVisibility(true);
            mActivity.setSupportProgressBarVisibility(true);
        } else if(count==0) {
            Log.d(TAG, "Calling loading stop");
            mActivity.setSupportProgressBarIndeterminateVisibility(false);
            mActivity.setSupportProgressBarVisibility(false);
        }
    }
}
