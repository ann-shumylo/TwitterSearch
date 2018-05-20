package com.itrex.tasks.twittersearch.twitter.responses;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/19/18
 */
public class AuthResponse {
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("access_token")
    private String accessToken;

    public AuthResponse() {
    }

    public String getAccessToken() {
        return accessToken;
    }
}
