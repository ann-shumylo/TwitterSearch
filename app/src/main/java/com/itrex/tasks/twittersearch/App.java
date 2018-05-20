package com.itrex.tasks.twittersearch;

import android.app.Application;

import com.itrex.tasks.twittersearch.utils.Prefs;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class App extends Application {
    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        Prefs.init(this);
    }

    public static Application getInstance() {
        return mInstance;
    }
}
