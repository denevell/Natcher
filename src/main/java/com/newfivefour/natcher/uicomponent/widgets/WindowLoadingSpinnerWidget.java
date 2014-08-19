package com.newfivefour.natcher.uicomponent.widgets;

import android.app.Activity;
import android.util.Log;

/**
 * Can be used if we're using something other than the swipe to refresh pattern for loading.
 */
public class WindowLoadingSpinnerWidget implements com.newfivefour.natcher.uicomponent.LoadingComponent {

    private static int count = 0;
    private static final String TAG = WindowLoadingSpinnerWidget.class.getSimpleName();
    private final Activity mActivity;

    public WindowLoadingSpinnerWidget(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void loading(boolean start) {
        if(start) {
            count++;
        } else {
            count--;
        }
        Log.d(TAG, "Loading count is: " + count);
        if(mActivity==null) return;
        if(start) {
            Log.d(TAG, "Calling loading start");
            mActivity.setProgressBarIndeterminateVisibility(true);
            mActivity.setProgressBarVisibility(true);
        } else if(count==0) {
            Log.d(TAG, "Calling loading stop");
            mActivity.setProgressBarIndeterminateVisibility(false);
            mActivity.setProgressBarVisibility(false);
        }
    }
}
