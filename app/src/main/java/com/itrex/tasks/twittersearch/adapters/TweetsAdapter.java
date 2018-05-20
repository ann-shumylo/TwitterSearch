package com.itrex.tasks.twittersearch.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.itrex.tasks.twittersearch.R;
import com.itrex.tasks.twittersearch.twitter.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ItemViewHolder> {
    private List<Tweet> mItems = new ArrayList<>();

    public TweetsAdapter() {
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Tweet tweet = mItems.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setTweets(List<Tweet> tweets) {
        mItems.clear();
        mItems.addAll(tweets);
        notifyDataSetChanged();
    }

    public void addTweet(Tweet tweet) {
        mItems.add(tweet);
        notifyDataSetChanged();
    }

    public void addTweets(List<Tweet> tweets) {
        mItems.addAll(tweets);
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_avatar) ImageView mAvatar;
        @BindView(R.id.user_name) TextView mUserName;
        @BindView(R.id.tweet_text) TextView mText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Tweet tweet) {
            RequestOptions options = new RequestOptions()
                    .circleCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(itemView.getContext()).load(tweet.getProfileImageUrl()).apply(options).into(mAvatar);

            mUserName.setText(tweet.getUserName());
            mText.setText(tweet.getText());
        }
    }
}
