package com.newfivefour.natcher.uicomponent.widgets;

import android.content.Context;
import android.widget.Toast;

import com.newfivefour.natcher.uicomponent.events.OnRefresh;
import com.newfivefour.natcher.uicomponent.views.ServerErrorView;

public class TextViewServerErrorWidget implements ServerErrorView {

    private final Context mApplicationContext;

    public TextViewServerErrorWidget(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    @Override public void setRefreshableConnector(OnRefresh connector) { }
    @Override
    public void showServerError(boolean show, int code, String message) {
        if (show) {
            Toast.makeText(mApplicationContext, "A server error,  called " + code + " - oh no: " + message, Toast.LENGTH_LONG).show();
        }
    }
}
