package com.itrex.tasks.twittersearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itrex.tasks.twittersearch.models.Tweet;

import java.util.List;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/19/18
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private List<Tweet> mResultsList;

    public TweetsAdapter(List<Tweet> list) {
        mResultsList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Tweet tweet = mResultsList.get(position);

        holder.mUserName.setText(tweet.getUserName());
        holder.mText.setText(tweet.getText());
    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserName;
        private TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.user_name);
            mText = itemView.findViewById(R.id.tweet_text);
        }
    }
}
