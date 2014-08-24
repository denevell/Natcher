package com.newfivefour.natcher.screens.enterpost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.services.PostsRecentService;
import com.newfivefour.natcher.uicomponent.Populatable;
import com.newfivefour.natcher.uicomponent.UiComponentDelegate;
import com.newfivefour.natcher.uicomponent.UiComponentVanilla;

public class PostAddView extends FrameLayout implements
        UiComponentDelegate<PostsRecentService.RecentPosts>,
        Populatable<PostsRecentService.RecentPosts> {

    private final EditText mContentEditText;
    private UiComponentVanilla<PostsRecentService.RecentPosts> mUIComponent;
    private ListView mListView;

    public PostAddView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.post_add_view, this, true);
        mContentEditText = (EditText) findViewById(R.id.post_add_edittext);
    }

    @SuppressWarnings("unused")
    public PostAddView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public UiComponentVanilla<PostsRecentService.RecentPosts> getUiComponentDelegate() {
        return null;
    }

    @Override
    public void populateWithContent(PostsRecentService.RecentPosts ob) {
    }

    @Override
    public boolean isContentEmpty() {
        return false;
    }

    @Override
    public void clearContent() {
    }

    public String getPostContent() {
        return mContentEditText.getText().toString();
    }
}
