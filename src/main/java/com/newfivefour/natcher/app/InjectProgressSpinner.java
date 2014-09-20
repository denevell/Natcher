package com.newfivefour.natcher.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Re-parents a view, giving it a progress spinner, with the ability to specify the alignment and style.
 *
 * If you're using the view's id for alignment, you need to wrap it in something else, like a
 * FrameLayout and use the id on that instead.
 */
public class InjectProgressSpinner {

    public static void inject(View view,
                              boolean showSpinner) {
        inject(view,
                showSpinner,
                android.R.attr.progressBarStyle,
                0, 0, 0, 0,
                RelativeLayout.ALIGN_PARENT_RIGHT,
                RelativeLayout.CENTER_VERTICAL);
    }

    /**
     *
     * @param progressStyle Needs to be an <attr name="something".. /> in your attr.xml and that 'something' is defined in your styles.xml
     */
    public static void inject(View view,
                              boolean showSpinner,
                              int progressStyle) {
        inject(view,
                showSpinner,
                progressStyle,
                0, 0, 0, 0,
                RelativeLayout.ALIGN_PARENT_RIGHT,
                RelativeLayout.CENTER_VERTICAL
        );
    }

    /**
     * @param progressStyle Needs to be an <attr name="something" type="reference" /> in your attr.xml and that 'something' is defined in your styles.xml
     */
    public static void inject(View view,
                              boolean showSpinner,
                              int progressStyle,
                              int paddingLeft,
                              int paddingRight,
                              int paddingTop,
                              int paddingBottom,
                              int ...relativeLayoutAlignParent) {
        // Get the view's parent
        Context context = view.getContext();
        ViewGroup parent = (ViewGroup) view.getParent();
        int indexOfOldView = parent.indexOfChild(view);
        ViewGroup.LayoutParams oldLayoutParams = view.getLayoutParams();

        // If we've already added the progress spinner, either show it or hide it
        if(parent.getTag()!=null && parent.getTag().equals("injected_progress_spinner")) {
            View injected = parent.findViewWithTag("injected_spinner");
            if(injected!=null && !showSpinner) {
                fadeOut(injected);
            } else if(injected!=null && showSpinner) {
                fadeIn(injected);
            }
            return;
        }

        // Create layout for new container
        RelativeLayout rl = new RelativeLayout(context);
        rl.setTag("injected_progress_spinner");
        rl.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // So we've not already added a spinner, so remove the current view from its parent and add it
        // to the new container
        // Add the view into the new container
        parent.removeView(view);
        rl.addView(view);

        // Create the progress spinner
        ProgressBar tv = new ProgressBar(context, null, progressStyle);
        tv.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        tv.setTag("injected_spinner");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if(relativeLayoutAlignParent!=null) {
            for (int rule : relativeLayoutAlignParent) {
                params.addRule(rule);
            }
        }
        rl.addView(tv, params);

        // Add new layout into view's old container
        parent.addView(rl, indexOfOldView, oldLayoutParams);

        // Start fade in animation
        fadeIn(tv);
    }

    public static void fadeIn(View v) {
        v.setVisibility(View.VISIBLE);
        AlphaAnimation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setDuration(400);
        fadeIn.setFillAfter(true);
        v.startAnimation(fadeIn);
    }

    public static void fadeOut(View v) {
        AlphaAnimation fadeIn = new AlphaAnimation(1,0);
        fadeIn.setDuration(400);
        fadeIn.setFillAfter(true);
        v.startAnimation(fadeIn);
        v.setVisibility(View.INVISIBLE);
    }
}
