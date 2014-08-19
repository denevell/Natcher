package com.newfivefour.natcher.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.uicomponent.events.OnEmpty;
import com.newfivefour.natcher.uicomponent.events.OnEmptyConnector;
import com.newfivefour.natcher.uicomponent.events.OnRefresh;
import com.newfivefour.natcher.uicomponent.events.OnRefreshConnector;

public class EmptyViewGeneric extends FrameLayout implements
        OnRefreshConnector,
        OnEmptyConnector {

    private final View mRefreshButton;
    private final View mTextView;

    public EmptyViewGeneric(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyViewGeneric(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.empty_layout, this, true);
        mRefreshButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.empty_textview);
    }

    @Override
    public void setRefreshableConnector(final OnRefresh connector) {
        if(mRefreshButton!=null) {
            mRefreshButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    connector.onRefreshContent();
                }
            });
        }
    }

    @Override
    public void setEmptyConnector(final OnEmpty connector) {
        if(mTextView!=null) {
            mTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   connector.onIsEmpty();
                }
            });
        }
    }
}
