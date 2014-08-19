package com.newfivefour.natcher.uicomponent.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.newfivefour.natcher.uicomponent.EmptiableComponent;
import com.newfivefour.natcher.uicomponent.EmptiableContentConnector;
import com.newfivefour.natcher.uicomponent.EmptyComponent;
import com.newfivefour.natcher.uicomponent.LoadingComponent;
import com.newfivefour.natcher.uicomponent.Refreshable;
import com.newfivefour.natcher.uicomponent.RefreshableConnector;
import com.newfivefour.natcher.uicomponent.ServerErrorComponent;

public class LoadingErrorEmptyWidget implements
        RefreshableConnector,
        EmptiableContentConnector,
        LoadingComponent,
        EmptyComponent,
        ServerErrorComponent {
    private View mError;
    private View mEmpty;
    private View mLoading;

    public LoadingErrorEmptyWidget(ViewGroup view,
                                   int errorLayout,
                                   int emptyLayout) {
        this(view, -1, errorLayout, emptyLayout);
    }

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
    public void loading(boolean start) {
        setVisibleOrInvisible(mLoading, start);
    }

    @Override
    public void empty(boolean empty) {
        setVisibleOrInvisible(mEmpty, empty);
    }

    @Override
    public void serverError(boolean show) {
        setVisibleOrInvisible(mError, show);
    }

    @Override
    public void setRefreshableConnector(final Refreshable connector) {
        if(connector==null) return;
        if(mError!=null && mError instanceof RefreshableConnector) {
            ((RefreshableConnector)mError).setRefreshableConnector(connector);
        }
        if(mEmpty !=null && mEmpty instanceof RefreshableConnector) {
            ((RefreshableConnector)mEmpty).setRefreshableConnector(connector);
        }
    }

    @Override
    public void setEmptyConnector(EmptiableComponent connector) {
        if(connector==null) return;
        if(mEmpty !=null && mEmpty instanceof EmptiableContentConnector) {
            ((EmptiableContentConnector)mEmpty).setEmptyConnector(connector);
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
