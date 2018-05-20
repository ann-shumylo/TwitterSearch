package com.itrex.tasks.twittersearch.twitter.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anna Pliskovskaya (anna.pliskovskaya@gmail.com)
 * @since 5/20/18
 */
public class SearchMetadata {
    @SerializedName("next_results")
    private String nextResults;

    @SerializedName("query")
    private String query;

    @SerializedName("count")
    private long count;

    public String getNextResults() {
        return nextResults;
    }

    public void setNextResults(String nextResults) {
        this.nextResults = nextResults;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
