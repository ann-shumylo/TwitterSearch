package com.itrex.tasks.twittersearch.presenter;

import android.text.TextUtils;

import com.itrex.tasks.twittersearch.TwitterClient;
import com.itrex.tasks.twittersearch.contract.MainViewContract;
import com.itrex.tasks.twittersearch.twitter.models.Tweet;
import com.itrex.tasks.twittersearch.utils.Prefs;
import com.itrex.tasks.twittersearch.utils.TwitterUtils;

import java.util.List;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class MainViewPresenter implements MainViewContract.IMainViewPresenter {
    private MainViewContract.IMainView mView;
    private TwitterClient mTwitterClient = new TwitterClient();

    private String mAccessToken = Prefs.get().getAccessToken();

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
                //TODO
            }
        });
    }

    private void searchTweets(String accessToken, String searchTerm) {
        mTwitterClient.searchTweets(accessToken, searchTerm, new TwitterClient.SearchResultListener() {
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
