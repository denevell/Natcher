package com.newfivefour.natcher.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class InjectProgressSpinner {
    public static void inject(View childView, boolean showSpinner) {
        Context context = childView.getContext();
        ViewGroup parent = (ViewGroup) childView.getParent();
        if(parent.getTag()!=null && parent.getTag().equals("injected_progress_spinner")) {
            View injected = parent.findViewWithTag("injected_spinner");
            if(injected!=null && !showSpinner) {
                injected.setVisibility(View.GONE);
            } else if(injected!=null && showSpinner) {
                injected.setVisibility(View.VISIBLE);
            }
            return;
        }
        int indexOfOldView = parent.indexOfChild(childView);
        ViewGroup.LayoutParams oldLayoutParams = childView.getLayoutParams();
        parent.removeView(childView);

        // Create layout for new container
        RelativeLayout rl = new RelativeLayout(context);
        rl.setTag("injected_progress_spinner");
        rl.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Add the view into the new container
        rl.addView(childView);

        // Create the progress spinner
        ProgressBar tv = new ProgressBar(context);
        tv.setTag("injected_spinner");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        rl.addView(tv, params);

        // Add new layout into view's old container
        parent.addView(rl, indexOfOldView, oldLayoutParams);
    }
}
