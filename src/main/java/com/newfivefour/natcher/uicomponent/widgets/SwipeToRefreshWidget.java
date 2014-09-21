package com.newfivefour.natcher.uicomponent.widgets;

import android.support.v4.widget.SwipeRefreshLayout;

import com.newfivefour.natcher.uicomponent.events.OnRefreshCallback;
import com.newfivefour.natcher.uicomponent.events.OnRefreshWidget;
import com.newfivefour.natcher.uicomponent.views.LoadingDisplay;

/**
 * Takes in a SwipeRefreshLayout and gives it the OnRefresh callback
 * when it's set, usually by the UiComponent, for use when the user
 * swipes downwards.
 *
 * It can be also used as a LoadingView, the UiComponent will set the
 * setRefreshing() method if it's used as such.
 */
public class SwipeToRefreshWidget implements OnRefreshWidget, LoadingDisplay {
    private final SwipeRefreshLayout mSwipe;

    @SuppressWarnings("deprecated")
    public SwipeToRefreshWidget(SwipeRefreshLayout swipe) {
        mSwipe = swipe;
        mSwipe.setColorScheme(
                android.R.color.holo_red_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_green_light
        );
    }

    @Override
    public void setRefreshableCallback(final OnRefreshCallback connector) {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipe.setRefreshing(false);
                connector.onRefreshContent();
            }
        });
    }

    @Override
    public void showLoading(boolean start) {
        if(start) {
            mSwipe.setRefreshing(true);
        } else {
            mSwipe.setRefreshing(false);
        }
    }
}
