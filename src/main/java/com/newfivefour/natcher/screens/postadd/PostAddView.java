package com.newfivefour.natcher.screens.postadd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.app.InjectProgressSpinner;
import com.newfivefour.natcher.models.PostAdded;
import com.newfivefour.natcher.uicomponent.Populatable;
import com.newfivefour.natcher.uicomponent.UiComponentDelegate;
import com.newfivefour.natcher.uicomponent.UiComponentVanilla;
import com.newfivefour.natcher.uicomponent.views.LoadingDisplay;
import com.newfivefour.natcher.uicomponent.widgets.HideKeyboardWidget;
import com.newfivefour.natcher.uicomponent.widgets.TextViewServerErrorWidget;

public class PostAddView extends FrameLayout implements
        UiComponentDelegate<PostAdded>,
        Populatable<PostAdded> {

    private static final String TAG = PostAddView.class.getSimpleName();
    private final EditText mContentEditText;
    private final EditText mSubjectEditText;
    private UiComponentVanilla<PostAdded> mUIComponent;

    public PostAddView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.post_add_view, this, true);
        mContentEditText = (EditText) findViewById(R.id.post_add_content_edittext);
        mSubjectEditText = (EditText) findViewById(R.id.post_add_subject_edittext);
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
    public void populateOnSuccessfulResponse(PostAdded ob) {
        mContentEditText.setText("");
        mSubjectEditText.setText("");
    }

    @Override
    public void clearContentOnEmptyResponse() {
    }

    @Override
    public boolean showInComponentLoading() {
        return true;
    }

    @Override
    public boolean showOutOfComponentLoading() {
        return true;
    }

    @Override
    public boolean showInComponentServerError() {
        return true;
    }

    @Override
    public boolean showOutOfComponentServerError() {
        return true;
    }

    private void createUiComponent() {
        mUIComponent = new UiComponentVanilla<>(this);
        mUIComponent.setInComponentLoadingDisplay(new LoadingDisplay() {
            @Override
            public void showLoading(boolean show) {
                mContentEditText.setEnabled(!show);
                mSubjectEditText.setEnabled(!show);
                InjectProgressSpinner.inject(mContentEditText, show, R.attr.progressBarMy,
                        0, 0, 0, 0,
                        RelativeLayout.CENTER_IN_PARENT);
            }
        });
        TextViewServerErrorWidget textViewServerErrorWidget = new TextViewServerErrorWidget(getContext().getApplicationContext());
        mUIComponent.setInComponentServerErrorDisplay(textViewServerErrorWidget);
        mUIComponent.setOutOfComponentServerErrorDisplay(textViewServerErrorWidget);
        mUIComponent.setHideKeyboard(new HideKeyboardWidget(this));
    }

    public String getPostContent() {
        return mContentEditText.getText().toString();
    }

    public String getPostSubject() {
        return mSubjectEditText.getText().toString();
    }
}
