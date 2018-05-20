package com.itrex.tasks.twittersearch.twitter.responses;

import com.google.gson.annotations.SerializedName;
import com.itrex.tasks.twittersearch.twitter.models.SearchMetadata;
import com.itrex.tasks.twittersearch.twitter.models.Tweet;

import java.util.List;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/19/18
 */
public class SearchResponse {
    @SerializedName("statuses")
    private List<Tweet> statuses;
    @SerializedName("search_metadata")
    private SearchMetadata searchMetadata;

    public SearchResponse() {
    }

    public List<Tweet> getStatuses() {
        return statuses;
    }

    public SearchMetadata getSearchMetadata() {
        return searchMetadata;
    }
}
