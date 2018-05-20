package com.itrex.tasks.twittersearch.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itrex.tasks.twittersearch.R;
import com.itrex.tasks.twittersearch.adapters.TweetsAdapter;
import com.itrex.tasks.twittersearch.contract.MainViewContract;
import com.itrex.tasks.twittersearch.presenter.MainViewPresenter;
import com.itrex.tasks.twittersearch.twitter.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainViewContract.IMainView {
    private MainViewContract.IMainViewPresenter mPresenter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView.Adapter mAdapter;
    private List<Tweet> mResultsList = new ArrayList<>();

    @BindView(R.id.search_edit_txt) EditText mSearchEditText;
    @BindView(R.id.empty_view) TextView mEmptyView;
    @BindView(R.id.results_recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new TweetsAdapter(mResultsList);
        prepareRecyclerView();

        mPresenter = new MainViewPresenter(this);
    }

    private void prepareRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        final DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.search_btn)
    public void search(View view) {
        if (mPresenter != null) {
            String searchTerm = mSearchEditText.getText().toString();
            mPresenter.onSearchButtonPressed(searchTerm);
        }
    }

    @Override
    public void showEmptyFieldMessage() {
        Toast.makeText(getApplicationContext(), R.string.search_empty_field_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerErrorMessage() {
        Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearList() {
        mResultsList.clear();
    }

    @Override
    public void showTweets(List<Tweet> tweets) {
        if (tweets.size() > 0) {
            mEmptyView.setVisibility(View.GONE);
            mResultsList.addAll(tweets);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }
}
