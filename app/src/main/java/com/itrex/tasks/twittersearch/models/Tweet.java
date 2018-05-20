package com.itrex.tasks.twittersearch.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/19/18
 */
public class Tweet {
    @SerializedName("id")
    private String id;
    @SerializedName("text")
    private String text;
    @SerializedName("user")
    private User user;

    public Tweet() {
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        if (user == null) return "FAIL";

        return user.getScreenName();
    }
}
