package com.itrex.tasks.twittersearch.utils;

import android.util.Base64;

import com.itrex.tasks.twittersearch.BuildConfig;

import java.nio.charset.StandardCharsets;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public final class TwitterUtils {
    private TwitterUtils() {
    }

    public static String getEncodedBearerToken() {
        String text = BuildConfig.TWITTER_CUSTOMER_KEY + ":" + BuildConfig.TWITTER_CUSTOMER_SECRET;
        byte[] data = text.getBytes(StandardCharsets.UTF_8);

        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

}
