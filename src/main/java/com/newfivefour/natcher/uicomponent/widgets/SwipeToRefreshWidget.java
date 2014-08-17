package com.newfivefour.natcher.uicomponent.widgets;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;

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

    public void fixListView(AbsListView listView) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    mSwipe.setEnabled(true);
                } else {
                    mSwipe.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void setRefreshableConnector(final Refreshable connector) {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connector.onRefreshContent();
            }
        });
    }

    @Override
    public void loadingStart(boolean start) {
        if(start) {
            mSwipe.setRefreshing(true);
        } else {
            mSwipe.setRefreshing(false);
        }
    }
}
