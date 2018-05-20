package com.itrex.tasks.twittersearch.presenter;

import android.text.TextUtils;

import com.itrex.tasks.twittersearch.contract.MainViewContract;
import com.itrex.tasks.twittersearch.twitter.APIClient;
import com.itrex.tasks.twittersearch.twitter.models.Tweet;
import com.itrex.tasks.twittersearch.twitter.responses.AuthResponse;
import com.itrex.tasks.twittersearch.twitter.responses.SearchResponse;
import com.itrex.tasks.twittersearch.twitter.service.AuthService;
import com.itrex.tasks.twittersearch.twitter.service.SearchService;
import com.itrex.tasks.twittersearch.utils.Prefs;
import com.itrex.tasks.twittersearch.utils.TwitterUtils;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class MainViewPresenter implements MainViewContract.IMainViewPresenter {
    private MainViewContract.IMainView mView;

    private AuthService mAuthService;
    private SearchService mSearchService;

    private String mAccessToken = Prefs.get().getAccessToken();

    public MainViewPresenter(MainViewContract.IMainView view) {
        mView = view;

        mAuthService = APIClient.getClient().create(AuthService.class);
        mSearchService = APIClient.getClient().create(SearchService.class);

        if (!isAccessTokenExist()) {
            fetchAccessToken();
        }
    }

    private void fetchAccessToken() {
        mAuthService.auth("Basic " + TwitterUtils.getEncodedBearerToken()).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                final AuthResponse body = response.body();
                if (body != null) {
                    String token = body.getAccessToken();
                    setAccessToken(token);
                    saveAccessToken(token);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                //TODO
            }
        });
    }

    private void searchTweets(String accessToken, String searchTerm) {
        mSearchService.getSearchResults("Bearer " + accessToken, searchTerm, "popular", null).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        showEmptyFieldMessage();
                    }
                } else {

                    final SearchResponse body = response.body();
                    if (body != null && mView != null) {
                        final List<Tweet> tweets = body.getStatuses();
                        mView.showTweets(tweets);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                showServerErrorMessage();
            }
        });
    }

    private void showServerErrorMessage() {
        if (mView != null) {
            mView.showServerErrorMessage();
        }
    }

    private void showEmptyFieldMessage() {
        if (mView != null) {
            mView.showEmptyFieldMessage();
        }
    }

    private void saveAccessToken(String token) {
        Prefs.get().setAccessToken(token);
    }

    @Override
    public void onSearchButtonPressed(String searchTerm) {
        if (!isAccessTokenExist()) {
            fetchAccessToken();
            return;
        }
        clearResultList();
        searchTweets(getAccessToken(), searchTerm);
    }

    private void clearResultList() {
        if (mView != null) {
            mView.clearList();
        }
    }

    private boolean isAccessTokenExist() {
        return !TextUtils.isEmpty(getAccessToken());
    }

    private String getAccessToken() {
        return mAccessToken;
    }

    private void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }
}
