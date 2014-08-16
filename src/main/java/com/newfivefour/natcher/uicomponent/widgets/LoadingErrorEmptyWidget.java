package com.newfivefour.natcher.uicomponent.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.newfivefour.natcher.uicomponent.EmptiableComponent;
import com.newfivefour.natcher.uicomponent.EmptiableContentConnector;
import com.newfivefour.natcher.uicomponent.Refreshable;
import com.newfivefour.natcher.uicomponent.RefreshableConnector;

public class LoadingErrorEmptyWidget implements
        RefreshableConnector,
        EmptiableContentConnector {
    private final View mError;
    private final View mEmpty;
    private View mLoading;

    public LoadingErrorEmptyWidget(ViewGroup view,
                                   int loadingLayout,
                                   int errorLayout,
                                   int emptyLayout) {
        // Load views
        mLoading = LayoutInflater.from(view.getContext()).inflate(loadingLayout, view, false);
        mEmpty = LayoutInflater.from(view.getContext()).inflate(emptyLayout, view, false);
        mError = LayoutInflater.from(view.getContext()).inflate(errorLayout, view, false);
        // Set layout params
        if (view instanceof FrameLayout) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLoading.setLayoutParams(params);
            mError.setLayoutParams(params);
            mEmpty.setLayoutParams(params);
        } else if (view instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLoading.setLayoutParams(params);
            mError.setLayoutParams(params);
            mEmpty.setLayoutParams(params);
        } else if (view instanceof ViewGroup) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLoading.setLayoutParams(params);
            mError.setLayoutParams(params);
            mEmpty.setLayoutParams(params);
        }
        // Add views
        view.addView(mLoading);
        view.addView(mError);
        view.addView(mEmpty);
        mLoading.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.INVISIBLE);
        mEmpty.setVisibility(View.INVISIBLE);
    }

    public void showLoading(boolean show) {
       if(show) {
           mLoading.setVisibility(View.VISIBLE);
       } else {
           mLoading.setVisibility(View.INVISIBLE);
       }
    }

    public void showError(boolean show) {
        if(show) {
            mError.setVisibility(View.VISIBLE);
        } else {
            mError.setVisibility(View.INVISIBLE);
        }
    }

    public void showEmpty(boolean show) {
        if(show) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setRefreshableConnector(final Refreshable connector) {
        if(connector==null) return;
        if(mError instanceof RefreshableConnector) {
            ((RefreshableConnector)mError).setRefreshableConnector(connector);
        }
        if(mEmpty instanceof RefreshableConnector) {
            ((RefreshableConnector)mEmpty).setRefreshableConnector(connector);
        }
    }

    @Override
    public void setEmptyConnector(EmptiableComponent connector) {
        if(connector==null) return;
        if(mEmpty instanceof EmptiableContentConnector) {
            ((EmptiableContentConnector)mEmpty).setEmptyConnector(connector);
        }
    }
}
