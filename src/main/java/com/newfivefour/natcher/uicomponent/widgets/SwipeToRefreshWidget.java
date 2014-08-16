package com.newfivefour.natcher.uicomponent.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.newfivefour.natcher.uicomponent.LoadingComponent;
import com.newfivefour.natcher.uicomponent.Refreshable;
import com.newfivefour.natcher.uicomponent.RefreshableConnector;

public class SwipeToRefreshWidget extends SwipeRefreshLayout
        implements RefreshableConnector, LoadingComponent {
    public SwipeToRefreshWidget(Context context) {
        this(context, null);
    }

    public SwipeToRefreshWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setColorScheme(
                android.R.color.holo_red_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_green_light
        );
    }

    public void fixListView(AbsListView listView) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
            }
        });

    }

    @Override
    public void setRefreshableConnector(final Refreshable connector) {
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                connector.onRefreshContent();
            }
        });
    }

    @Override
    public void loadingStart(boolean start) {
        if(start) {
            setRefreshing(true);
        } else {
            setRefreshing(false);
        }
    }
}
