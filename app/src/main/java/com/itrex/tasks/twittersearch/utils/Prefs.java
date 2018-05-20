package com.itrex.tasks.twittersearch.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String SETTINGS_NAME = "default_settings";

    private static final String KEY_ACCESS_TOKEN = "access_token";


    public static void init(Context context) {
        mInstance = new Prefs(context);
    }

    public static Prefs get() {
        return mInstance;
    }

    private static Prefs mInstance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private Prefs(Context context) {
        mPrefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    public void setAccessToken(String token) {
        mEditor.putString(KEY_ACCESS_TOKEN, token);
        mEditor.apply();
    }

    public String getAccessToken() {
        return mPrefs.getString(KEY_ACCESS_TOKEN, "");
    }
}

