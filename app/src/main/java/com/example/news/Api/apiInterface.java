package com.example.news.Api;

import com.example.news.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface apiInterface {

    @GET("top-headlines")
    Call<News> getNews(
        @Query("country") String country ,
        @Query("apiKey") String apiKey
    );


    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") String keyword ,
            @Query("language") String language ,
            @Query("sortBy") String sortBy ,
            @Query("apiKey") String apiKey
    );
}
