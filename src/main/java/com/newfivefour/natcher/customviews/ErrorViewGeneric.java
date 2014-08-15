package com.newfivefour.natcher.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.app.component.RefreshableComponent;
import com.newfivefour.natcher.app.component.RefreshableContentConnector;

public class ErrorViewGeneric extends FrameLayout implements RefreshableContentConnector {

    public ErrorViewGeneric(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorViewGeneric(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.error_layout, this, true);
    }

    @Override
    public void setRefreshConnector(final RefreshableComponent connector) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connector.onRefreshContent();
            }
        });
    }
}
