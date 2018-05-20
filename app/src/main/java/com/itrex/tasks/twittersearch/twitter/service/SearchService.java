package com.itrex.tasks.twittersearch.twitter.service;

import com.itrex.tasks.twittersearch.twitter.responses.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/18/18
 */
public interface SearchService {
    @GET("/1.1/search/tweets.json?")
    Call<SearchResponse> getSearchResults(@Header("Authorization") String authorization,
                                          @Query("q") String term, @Query("count") int count);
}