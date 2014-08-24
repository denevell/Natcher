package com.newfivefour.natcher.screens.postadd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.models.PostAdded;
import com.newfivefour.natcher.uicomponent.Populatable;
import com.newfivefour.natcher.uicomponent.UiComponentDelegate;
import com.newfivefour.natcher.uicomponent.UiComponentVanilla;
import com.newfivefour.natcher.uicomponent.widgets.LoadingErrorEmptyWidget;
import com.newfivefour.natcher.uicomponent.widgets.TextViewServerErrorWidget;

public class PostAddView extends FrameLayout implements
        UiComponentDelegate<PostAdded>,
        Populatable<PostAdded> {

    private static final String TAG = PostAddView.class.getSimpleName();
    private final EditText mContentEditText;
    private UiComponentVanilla<PostAdded> mUIComponent;

    public PostAddView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.post_add_view, this, true);
        mContentEditText = (EditText) findViewById(R.id.post_add_edittext);
        createUiComponent();
    }

    @SuppressWarnings("unused")
    public PostAddView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public UiComponentVanilla<PostAdded> getUiComponentDelegate() {
        return mUIComponent;
    }

    @Override
    public void populateWithContent(PostAdded ob) {
    }

    @Override
    public boolean shouldShowInComponentLoading() {
        return true;
    }

    @Override
    public void clearContent() {
    }

    private void createUiComponent() {
        mUIComponent = new UiComponentVanilla<>(this);
        LoadingErrorEmptyWidget loadingWidget = new LoadingErrorEmptyWidget(this, R.layout.loading_layout, -1, -1);
        loadingWidget.showLoading(false);
        mUIComponent.setInComponentLoadingDisplay(loadingWidget);
        TextViewServerErrorWidget textViewServerErrorWidget = new TextViewServerErrorWidget(getContext().getApplicationContext());
        mUIComponent.setInComponentServerErrorDisplay(textViewServerErrorWidget);
        mUIComponent.setInComponentServerErrorDisplayForUseWhenWeHaveContent(textViewServerErrorWidget);
    }

    public String getPostContent() {
        return mContentEditText.getText().toString();
    }
}
