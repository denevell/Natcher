package com.newfivefour.natcher.uicomponent.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.newfivefour.natcher.uicomponent.events.OnEmpty;
import com.newfivefour.natcher.uicomponent.events.OnEmptyConnector;
import com.newfivefour.natcher.uicomponent.events.OnRefresh;
import com.newfivefour.natcher.uicomponent.events.OnRefreshConnector;
import com.newfivefour.natcher.uicomponent.views.EmptyView;
import com.newfivefour.natcher.uicomponent.views.LoadingView;
import com.newfivefour.natcher.uicomponent.views.ServerErrorView;

/**
 * Can be used a LoadingView, ServerErrorView and EmptyView as a quick
 * one-stop shop when setting up a UiComponen.
 */
public class LoadingErrorEmptyWidget implements
        OnRefreshConnector,
        OnEmptyConnector,
        LoadingView,
        EmptyView,
        ServerErrorView {
    private View mError;
    private View mEmpty;
    private View mLoading;

    /**
     * @param errorLayout Should be something that implements OnRefreshConnector to receive a callback about refreshes
     * @param emptyLayout Should be something that implements On{Refresh,Empty}Connector to receive such callback
     */
    public LoadingErrorEmptyWidget(ViewGroup view,
                                   int errorLayout,
                                   int emptyLayout) {
        this(view, -1, errorLayout, emptyLayout);
    }

    /**
     * @param errorLayout Should be something that implements OnRefreshConnector to receive a callback about refreshes
     * @param emptyLayout Should be something that implements On{Refresh,Empty}Connector to receive such callback
     */
    public LoadingErrorEmptyWidget(ViewGroup view,
                                   int loadingLayout,
                                   int errorLayout,
                                   int emptyLayout) {
        // Load views
        if(loadingLayout>0) {
            mLoading = LayoutInflater.from(view.getContext()).inflate(loadingLayout, view, false);
        }
        if(emptyLayout>0) {
            mEmpty = LayoutInflater.from(view.getContext()).inflate(emptyLayout, view, false);
        }
        if(errorLayout>0) {
            mError = LayoutInflater.from(view.getContext()).inflate(errorLayout, view, false);
        }
        // Set layout params
        if (view instanceof FrameLayout) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setLayoutParams(mLoading, params);
            setLayoutParams(mError, params);
            setLayoutParams(mEmpty, params);
        } else if (view instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setLayoutParams(mLoading, params);
            setLayoutParams(mError, params);
            setLayoutParams(mEmpty, params);
        } else if (view instanceof ViewGroup) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setLayoutParams(mLoading, params);
            setLayoutParams(mError, params);
            setLayoutParams(mEmpty, params);
        }
        // Add views
        if(mLoading!=null) {
            view.addView(mLoading);
        }
        if(mEmpty!=null) {
            view.addView(mEmpty);
        }
        if(mError!=null) {
            view.addView(mError);
        }
        setVisibleOrInvisible(mLoading, true);
        setVisibleOrInvisible(mError, true);
        setVisibleOrInvisible(mEmpty, true);
    }

    @Override
    public void showLoading(boolean start) {
        setVisibleOrInvisible(mLoading, start);
    }

    @Override
    public void showEmpty(boolean empty) {
        setVisibleOrInvisible(mEmpty, empty);
    }

    @Override
    public void showServerError(boolean show) {
        setVisibleOrInvisible(mError, show);
    }

    @Override
    public void setRefreshableConnector(final OnRefresh connector) {
        if(connector==null) return;
        if(mError!=null && mError instanceof OnRefreshConnector) {
            ((OnRefreshConnector)mError).setRefreshableConnector(connector);
        }
        if(mEmpty !=null && mEmpty instanceof OnRefreshConnector) {
            ((OnRefreshConnector)mEmpty).setRefreshableConnector(connector);
        }
    }

    @Override
    public void setEmptyConnector(OnEmpty connector) {
        if(connector==null) return;
        if(mEmpty !=null && mEmpty instanceof OnEmptyConnector) {
            ((OnEmptyConnector)mEmpty).setEmptyConnector(connector);
        }
    }

    private void setVisibleOrInvisible(View v, boolean show) {
        if(v==null) return;
        if(show) {
           v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.INVISIBLE);
        }
    }

    private void setLayoutParams(View v, ViewGroup.LayoutParams params) {
        if(v==null || params==null) return;
        mEmpty.setLayoutParams(params);
    }

}
