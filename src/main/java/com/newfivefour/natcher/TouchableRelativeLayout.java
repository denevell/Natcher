package com.newfivefour.natcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class TouchableRelativeLayout extends RelativeLayout implements SwipeInterface {

    private ActivitySwipeDetector mSwipeDetector;

    public TouchableRelativeLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        setFocusableInTouchMode(true);
        setFocusable(true);
        setClickable(true);
        mSwipeDetector = new ActivitySwipeDetector(this);
        setOnGenericMotionListener(new OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public void bottom2top(View v) {

    }

    @Override
    public void left2right(View v) {

    }

    @Override
    public void right2left(View v) {

    }

    @Override
    public void top2bottom(View v) {

    }
}
