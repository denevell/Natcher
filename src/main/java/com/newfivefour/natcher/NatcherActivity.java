package com.newfivefour.natcher;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


public class NatcherActivity extends ActionBarActivity {

    private TextView mTextView;
    private NatcherPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.natcher_activity);
        mTextView = (TextView) findViewById(R.id.textView);
        mPresenter = new NatcherPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void startLoading(boolean b) {
        setProgressBarIndeterminateVisibility(b);
        setProgressBarIndeterminate(b);
        setProgressBarVisibility(b);
    }
}
