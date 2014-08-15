package com.newfivefour.natcher.customviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.newfivefour.natcher.app.component.EmptiableComponent;
import com.newfivefour.natcher.app.component.EmptiableContentConnector;
import com.newfivefour.natcher.app.component.RefreshableComponent;
import com.newfivefour.natcher.app.component.RefreshableContentConnector;

public class LoadingErrorEmptyWidget implements
        RefreshableContentConnector,
        EmptiableContentConnector {
    private final View mError;
    private final View mEmpty;
    private View mLoading;
    private RefreshableComponent mRefreshCallback;

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
    public void setRefreshConnector(final RefreshableComponent connector) {
        if(connector==null) return;
        if(mError!=null && mError instanceof RefreshableContentConnector) {
            ((RefreshableContentConnector)mError).setRefreshConnector(connector);
        }
        if(mEmpty!=null && mEmpty instanceof RefreshableContentConnector) {
            ((RefreshableContentConnector)mEmpty).setRefreshConnector(connector);
        }
    }

    @Override
    public void setEmptyConnector(EmptiableComponent connector) {
        if(connector==null) return;
        if(mEmpty !=null && mEmpty instanceof EmptiableContentConnector) {
            ((EmptiableContentConnector)mEmpty).setEmptyConnector(connector);
        }
    }
}
