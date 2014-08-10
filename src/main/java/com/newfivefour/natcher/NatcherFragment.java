package com.newfivefour.natcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class NatcherFragment extends Fragment {

    private TextView mTextView;
    private NatcherPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new NatcherPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.natcher_fragment, container, false);
        mTextView = (TextView) v.findViewById(R.id.textView);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    public void startLoading(boolean b) {
        getActivity().setProgressBarIndeterminate(b);
        getActivity().setProgressBarVisibility(b);
    }
}
