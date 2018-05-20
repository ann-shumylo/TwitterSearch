package com.itrex.tasks.twittersearch;

import com.itrex.tasks.twittersearch.twitter.APIClient;
import com.itrex.tasks.twittersearch.twitter.models.Tweet;
import com.itrex.tasks.twittersearch.twitter.responses.AuthResponse;
import com.itrex.tasks.twittersearch.twitter.responses.SearchResponse;
import com.itrex.tasks.twittersearch.twitter.service.AuthService;
import com.itrex.tasks.twittersearch.twitter.service.SearchService;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class TwitterClient {
    private AuthService mAuthService;
    private SearchService mSearchService;

    public interface AuthorizeListener {
        void onSuccess(String accessToken);
        void onError(Throwable error);
    }

    public interface SearchResultListener {
        void onSuccess(List<Tweet> list);
        void onAuthError();
        void onError(Throwable error);
    }

    public TwitterClient() {
        mAuthService = APIClient.getClient().create(AuthService.class);
        mSearchService = APIClient.getClient().create(SearchService.class);
    }

    public void authorize(final String bearerToken, final AuthorizeListener listener) {
        mAuthService.auth("Basic " + bearerToken).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                final AuthResponse body = response.body();
                if (body != null && listener != null) {
                    String token = body.getAccessToken();
                    listener.onSuccess(token);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onError(t);
                }
            }
        });
    }

    public void searchTweets(String accessToken, String searchTerm, final SearchResultListener listener) {
        mSearchService.getSearchResults("Bearer " + accessToken, searchTerm, "popular", null).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (listener == null) return;

                if (!response.isSuccessful()) {
                    if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        listener.onAuthError();
                    }
                } else {
                    final SearchResponse body = response.body();
                    if (body != null) {
                        final List<Tweet> tweets = body.getStatuses();
                        listener.onSuccess(tweets);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onError(t);
                }
            }
        });
    }
}
