package com.itrex.tasks.twittersearch;

import android.app.Application;

import com.itrex.tasks.twittersearch.utils.Prefs;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Prefs.init(this);
    }
}
