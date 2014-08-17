package com.newfivefour.natcher.customviews;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * Like a normal SwipeRefreshLayout, but searches its view hierarchy to
 * find the first AbsListView, and uses that to detect if either the ListView
 * or the SwipeRefreshLayout should handle the scroll up motion.
 *
 * The default implementation only works if the AbsListView is the first child of
 * the SwipeRefreshLayout.
 */
public class SwipeRefreshLayoutWithListViewFinder extends SwipeRefreshLayout {

    public SwipeRefreshLayoutWithListViewFinder(Context context) {
        super(context);
    }

    public SwipeRefreshLayoutWithListViewFinder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        ViewGroup v = (ViewGroup) getChildAt(0);
        AbsListView absListView  = findListView(v);
        if(absListView!=null) {
            return absListView.getChildCount() > 0
                    && (absListView.getFirstVisiblePosition() > 0
                    || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
        } else {
            return super.canChildScrollUp();
        }
    }

    private AbsListView findListView(ViewGroup parent) {
        AbsListView abs = null;
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if(child instanceof AbsListView) {
                abs = (AbsListView) child;
                return abs;
            } else if (child instanceof ViewGroup) {
                AbsListView thisAbs = findListView((ViewGroup) child);
                if(thisAbs!=null) {
                    return thisAbs;
                }
            }
        }
        return null;
    }
}
