package com.itrex.tasks.twittersearch.contract;

import com.itrex.tasks.twittersearch.twitter.models.Tweet;

import java.util.List; /**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public interface MainViewContract {

    interface IMainViewPresenter {
        void onSearchButtonPressed(String searchTerm);
    }

    interface IMainView {
        void showEmptyFieldMessage();
        void showServerErrorMessage();
        void clearList();
        void showTweets(List<Tweet> tweets);
        void showAuthErrorMessage();
    }
}
