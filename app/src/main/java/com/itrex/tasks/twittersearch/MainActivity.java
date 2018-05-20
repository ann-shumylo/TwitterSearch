package com.itrex.tasks.twittersearch;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.itrex.tasks.twittersearch.responses.SearchResponse;
import com.itrex.tasks.twittersearch.service.AuthService;
import com.itrex.tasks.twittersearch.service.SearchService;
import com.itrex.tasks.twittersearch.models.Tweet;
import com.itrex.tasks.twittersearch.responses.AuthResponse;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mSharedPref;

    private EditText mSearchEditText;
    private ListView mListView;

    private AuthService mAuthService;
    private SearchService mSearchService;

    private TweetsAdapter mAdapter;

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
        mListView = findViewById(R.id.list_view);

        mAdapter = new TweetsAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);
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

        getTweets(accessToken, mSearchEditText.getText().toString());
    }

    private void getTweets(String accessToken, String searchTerm) {
        mSearchService.getPosts(accessToken, searchTerm, "popular", null).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        //TODO parse errorBody
                        Toast.makeText(getApplicationContext(), R.string.search_empty_field_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    final SearchResponse body = response.body();
                    if (body != null) {
                        final List<Tweet> tweets = body.getStatuses();
                        mAdapter.clear();
                        if (tweets.size() > 0) {
                            mAdapter.addAll(tweets);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.search_empty_tweets_list, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
