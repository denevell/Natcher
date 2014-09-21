package com.newfivefour.natcher.uicomponent.widgets;

import android.content.Context;
import android.widget.Toast;

import com.newfivefour.natcher.uicomponent.events.OnRefreshCallback;
import com.newfivefour.natcher.uicomponent.views.ServerErrorDisplay;

public class TextViewServerErrorWidget implements ServerErrorDisplay {

    private final Context mApplicationContext;

    public TextViewServerErrorWidget(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    @Override public void setRefreshableCallback(OnRefreshCallback connector) { }
    @Override
    public void showServerError(boolean show, int code, String message) {
        if (show) {
            Toast.makeText(mApplicationContext, "A server error,  called " + code + " - oh no: " + message, Toast.LENGTH_LONG).show();
        }
    }
}
