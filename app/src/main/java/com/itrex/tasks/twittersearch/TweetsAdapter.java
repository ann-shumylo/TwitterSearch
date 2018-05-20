package com.itrex.tasks.twittersearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itrex.tasks.twittersearch.models.Tweet;

import java.util.Collection;
import java.util.List;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/19/18
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {

    public TweetsAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet post = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mUserName = convertView.findViewById(R.id.user_name);
            viewHolder.mText = convertView.findViewById(R.id.text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (post != null) {
            viewHolder.mUserName.setText(post.getUserName());
            viewHolder.mText.setText(post.getText());
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView mUserName;
        private TextView mText;
    }
}
