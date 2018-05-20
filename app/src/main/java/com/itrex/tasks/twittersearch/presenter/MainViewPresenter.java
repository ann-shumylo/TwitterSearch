package com.itrex.tasks.twittersearch.presenter;

import android.text.TextUtils;

import com.itrex.tasks.twittersearch.twitter.TwitterClient;
import com.itrex.tasks.twittersearch.contract.MainViewContract;
import com.itrex.tasks.twittersearch.twitter.models.Tweet;
import com.itrex.tasks.twittersearch.utils.InternetUtils;
import com.itrex.tasks.twittersearch.utils.Prefs;
import com.itrex.tasks.twittersearch.utils.TwitterUtils;

import java.util.List;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class MainViewPresenter implements MainViewContract.IMainViewPresenter {
    public static final int TWEETS_COUNT = 100;

    private MainViewContract.IMainView mView;
    private TwitterClient mTwitterClient = new TwitterClient();

    private String mAccessToken = Prefs.get().getAccessToken();
    private String mSearchTerm = "";

    public MainViewPresenter(MainViewContract.IMainView view) {
        mView = view;

        if (!isAccessTokenExist()) {
            fetchAccessToken();
        }
    }

    private void fetchAccessToken() {
        mTwitterClient.authorize(TwitterUtils.getEncodedBearerToken(), new TwitterClient.AuthorizeListener() {
            @Override
            public void onSuccess(String accessToken) {
                setAccessToken(accessToken);
                saveAccessToken(accessToken);
            }

            @Override
            public void onError(Throwable error) {
                showAuthErrorMessage();
            }
        });
    }

    private void fetchTokenAndSearch() {
        mTwitterClient.authorize(TwitterUtils.getEncodedBearerToken(), new TwitterClient.AuthorizeListener() {
            @Override
            public void onSuccess(String accessToken) {
                setAccessToken(accessToken);
                saveAccessToken(accessToken);

                if (!TextUtils.isEmpty(mSearchTerm)) {
                    searchTweets(accessToken, mSearchTerm);
                }
            }

            @Override
            public void onError(Throwable error) {
                showAuthErrorMessage();
            }
        });
    }

    private void searchTweets(String accessToken, String searchTerm) {
        mTwitterClient.searchTweets(accessToken, searchTerm, TWEETS_COUNT, new TwitterClient.SearchResultListener() {
            @Override
            public void onSuccess(List<Tweet> list) {
                if (mView != null) {
                    mView.showTweets(list);
                }
            }

            @Override
            public void onAuthError() {
                showEmptyFieldMessage();
            }

            @Override
            public void onError(Throwable error) {
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

    private void showAuthErrorMessage() {
        if (mView != null) {
            mView.showAuthErrorMessage();
        }
    }

    private void showInternetUnavailableMessage() {
        if (mView != null) {
            mView.showInternetUnavailableMessage();
        }
    }

    private void saveAccessToken(String token) {
        Prefs.get().setAccessToken(token);
    }

    @Override
    public void onSearchButtonPressed(String searchTerm) {
        if (InternetUtils.isInternetAvailable()) {
            mSearchTerm = searchTerm;

            if (!isAccessTokenExist()) {
                fetchTokenAndSearch();
                return;
            }
            clearResultList();
            searchTweets(getAccessToken(), mSearchTerm);
        } else {
            showInternetUnavailableMessage();
        }
    }

    @Override
    public void onGoToSettingsClicked() {
        if (mView != null) {
            mView.showWiFiSettings();
        }
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
