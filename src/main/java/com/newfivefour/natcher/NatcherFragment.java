package com.newfivefour.natcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.newfivefour.natcher.services.PostsRecentService;


public class NatcherFragment extends android.app.Fragment {

    private ListView mListView;
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
        mListView = (ListView) v.findViewById(R.id.listView);
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

    public void setPosts(PostsRecentService.RecentPosts recentPosts) {
        ArrayAdapter adapter = new ArrayAdapter<PostsRecentService.RecentPosts.Post>(
                getActivity(),
                R.layout.post_list_item,
                recentPosts.getPosts());
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    public void startLoading(boolean b) {
        getActivity().setProgressBarIndeterminate(b);
        getActivity().setProgressBarVisibility(b);
    }
}
