package com.newfivefour.natcher.uicomponent.widgets;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class HideKeyboardWidget implements Runnable {

    private final View mView;

    public HideKeyboardWidget(View v) {
        mView = v;
    }

    @Override
    public void run() {
        try {
            InputMethodManager imm = (InputMethodManager) mView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
        } catch (Exception e) {}
    }
}
