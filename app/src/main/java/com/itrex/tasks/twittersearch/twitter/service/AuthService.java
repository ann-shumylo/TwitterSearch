package com.itrex.tasks.twittersearch.twitter.service;

import com.itrex.tasks.twittersearch.twitter.responses.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/19/18
 */
public interface AuthService {
    @POST("/oauth2/token?grant_type=client_credentials")
    Call<AuthResponse> auth(@Header("Authorization") String authorization);
}