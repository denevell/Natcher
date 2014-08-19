package com.newfivefour.natcher.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.uicomponent.events.OnRefresh;
import com.newfivefour.natcher.uicomponent.events.OnRefreshConnector;

public class ErrorViewGeneric extends FrameLayout implements OnRefreshConnector {

    public ErrorViewGeneric(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorViewGeneric(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.error_layout, this, true);
    }

    @Override
    public void setRefreshableConnector(final OnRefresh connector) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connector.onRefreshContent();
            }
        });
    }
}
