package com.newfivefour.natcher.uicomponent.widgets;

import android.support.v4.widget.SwipeRefreshLayout;

import com.newfivefour.natcher.uicomponent.LoadingComponent;
import com.newfivefour.natcher.uicomponent.Refreshable;
import com.newfivefour.natcher.uicomponent.RefreshableConnector;

public class SwipeToRefreshWidget implements RefreshableConnector, LoadingComponent {
    private final SwipeRefreshLayout mSwipe;

    public SwipeToRefreshWidget(SwipeRefreshLayout swipe) {
        mSwipe = swipe;
        mSwipe.setColorScheme(
                android.R.color.holo_red_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_green_light
        );
    }

    @Override
    public void setRefreshableConnector(final Refreshable connector) {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipe.setRefreshing(false);
                connector.onRefreshContent();
            }
        });
    }

    @Override
    public void loading(boolean start) {
        if(start) {
            mSwipe.setRefreshing(true);
        } else {
            mSwipe.setRefreshing(false);
        }
    }
}
