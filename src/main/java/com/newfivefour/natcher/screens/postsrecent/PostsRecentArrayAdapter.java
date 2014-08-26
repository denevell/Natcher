package com.newfivefour.natcher.screens.postsrecent;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.newfivefour.natcher.R;
import com.newfivefour.natcher.services.PostsRecentService;

import java.util.List;

public class PostsRecentArrayAdapter extends ArrayAdapter<PostsRecentService.RecentPosts.Post> {
    public PostsRecentArrayAdapter(Context context, int resource, List<PostsRecentService.RecentPosts.Post> posts) {
        super(context, resource, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.posts_list_item, parent, false);
        }
        TextView text = (TextView) convertView.findViewById(android.R.id.text1);

        // Model
        PostsRecentService.RecentPosts.Post item = getItem(position);
        String subject = item.getSubject();
        String content = item.getContent();

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(subject);
        builder.append(" ");
        builder.append(content);
        // Subject spannable
        TextAppearanceSpan subjectStyleable = new TextAppearanceSpan(getContext(), R.style.subject_text);
        builder.setSpan(subjectStyleable, 0, subject.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Content spannable
        TextAppearanceSpan contentStyleable = new TextAppearanceSpan(getContext(), R.style.content_text);
        builder.setSpan(contentStyleable, subject.length()+1, subject.length()+1+content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(builder);

        return convertView;
    }
}
