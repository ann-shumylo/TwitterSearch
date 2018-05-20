package com.itrex.tasks.twittersearch.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.itrex.tasks.twittersearch.R;
import com.itrex.tasks.twittersearch.twitter.models.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private List<Tweet> mResultsList;

    public TweetsAdapter(List<Tweet> list) {
        mResultsList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent, false);
            return new FooterViewHolder(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            return;
        }

        final Tweet tweet = mResultsList.get(position);
        ((ItemViewHolder)holder).bind(tweet);
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position != 0 && position == getItemCount() - 1;
    }

    @Override
    public int getItemCount() {
        if (mResultsList == null || mResultsList.size() == 0) {
            return 0;
        }
        return mResultsList.size() + 1;
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

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.footer_text)
        public void loadMore() {
            //TODO temp
            Toast.makeText(itemView.getContext(), "Load more", Toast.LENGTH_SHORT).show();
        }
    }
}
