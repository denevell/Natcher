package com.newfivefour.natcher.uicomponent.widgets;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.newfivefour.natcher.uicomponent.events.OnEmptyCallback;
import com.newfivefour.natcher.uicomponent.events.OnEmptyConnector;
import com.newfivefour.natcher.uicomponent.events.OnRefreshCallback;
import com.newfivefour.natcher.uicomponent.events.OnRefreshWidget;
import com.newfivefour.natcher.uicomponent.views.EmptyDisplay;
import com.newfivefour.natcher.uicomponent.views.LoadingDisplay;
import com.newfivefour.natcher.uicomponent.views.ServerErrorDisplay;

/**
 * Can be used a LoadingView, ServerErrorView and EmptyView as a quick
 * one-stop shop when setting up a UiComponent.
 *
 * Adds a error, empty and loading layout to end of the passed ViewGroup.
 */
public class LoadingErrorEmptyWidget implements
        OnRefreshWidget,
        OnEmptyConnector,
        LoadingDisplay,
        EmptyDisplay,
        ServerErrorDisplay {
    private static final String TAG = LoadingErrorEmptyWidget.class.getSimpleName();
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
     * Adds a error, empty and loading layout to end of the passed ViewGroup.
     *
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
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setLayoutParams(mLoading, params);
            setLayoutParams(mError, params);
            setLayoutParams(mEmpty, params);
        }
        // Add views
        if(mLoading!=null) {
            view.addView(mLoading, view.getChildCount());
        }
        if(mEmpty!=null) {
            view.addView(mEmpty, view.getChildCount());
        }
        if(mError!=null) {
            view.addView(mError, view.getChildCount());
        }
        setVisibleOrInvisible(mLoading, true);
        setVisibleOrInvisible(mError, true);
        setVisibleOrInvisible(mEmpty, true);
    }

    @Override
    public void showLoading(boolean show) {
        Log.d(TAG, "showLoading(): " + show);
        setVisibleOrInvisible(mLoading, show);
    }

    @Override
    public void showEmpty(boolean show) {
        setVisibleOrInvisible(mEmpty, show);
    }

    @Override
    public void showServerError(boolean show, int code, String message) {
        setVisibleOrInvisible(mError, show);
    }

    @Override
    public void setRefreshableCallback(final OnRefreshCallback connector) {
        if(connector==null) return;
        if(mError!=null && mError instanceof OnRefreshWidget) {
            ((OnRefreshWidget)mError).setRefreshableCallback(connector);
        }
        if(mEmpty !=null && mEmpty instanceof OnRefreshWidget) {
            ((OnRefreshWidget)mEmpty).setRefreshableCallback(connector);
        }
    }

    @Override
    public void setEmptyCallback(OnEmptyCallback connector) {
        if(connector==null) return;
        if(mEmpty !=null && mEmpty instanceof OnEmptyConnector) {
            ((OnEmptyConnector)mEmpty).setEmptyCallback(connector);
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
        v.setLayoutParams(params);
    }

}
