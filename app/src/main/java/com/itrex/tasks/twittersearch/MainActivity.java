package com.itrex.tasks.twittersearch;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itrex.tasks.twittersearch.responses.SearchResponse;
import com.itrex.tasks.twittersearch.service.AuthService;
import com.itrex.tasks.twittersearch.service.SearchService;
import com.itrex.tasks.twittersearch.models.Tweet;
import com.itrex.tasks.twittersearch.responses.AuthResponse;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mSharedPref;

    private EditText mSearchEditText;
    private TextView mEmptyView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private AuthService mAuthService;
    private SearchService mSearchService;

    private RecyclerView.Adapter mAdapter;
    private List<Tweet> mResultsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthService = APIClient.getClient().create(AuthService.class);
        mSearchService = APIClient.getClient().create(SearchService.class);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!mSharedPref.contains(BuildConfig.ACCESS_TOKEN)) {
            fetchAccessToken();
        }

        Button searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);

        mSearchEditText = findViewById(R.id.search_edit_txt);
        mRecyclerView = findViewById(R.id.results_recycler_view);
        mEmptyView = findViewById(R.id.empty_view);

        mAdapter = new TweetsAdapter(mResultsList);
        prepareRecyclerView();
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

    private void fetchAccessToken() {
        mAuthService.auth("Basic " + getEncodedBearerToken()).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                final AuthResponse body = response.body();
                if (body != null) {
                    saveAccessToken(body.getAccessToken());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });
    }

    private void saveAccessToken(String token) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(BuildConfig.ACCESS_TOKEN, "Bearer " + token);
        editor.apply();
    }

    private String getEncodedBearerToken() {
        String text = getString(R.string.twitter_customer_key) + ":" + getString(R.string.twitter_customer_secret);
        byte[] data = text.getBytes(StandardCharsets.UTF_8);

        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    @Override
    public void onClick(View view) {
        String accessToken = mSharedPref.getString(BuildConfig.ACCESS_TOKEN, "");
        if (accessToken.isEmpty()) {
            fetchAccessToken();
        }
        searchTweets(accessToken, mSearchEditText.getText().toString());
    }

    private void searchTweets(String accessToken, String searchTerm) {
        mSearchService.getSearchResults(accessToken, searchTerm, "popular", null).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        //TODO parse errorBody
                        Toast.makeText(getApplicationContext(), R.string.search_empty_field_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mResultsList.clear();
                    final SearchResponse body = response.body();
                    if (body != null) {
                        final List<Tweet> tweets = body.getStatuses();
                        if (tweets.size() > 0) {
                            mEmptyView.setVisibility(View.GONE);
                            mResultsList.addAll(tweets);
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
