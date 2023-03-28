package com.quochungcyou.proconnect.APIUtils;


import com.quochungcyou.proconnect.Model.ResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface {


    @GET("api/1/news")
    Call<ResultModel> getLastestHeadline(

            @Query("apikey") String apikey,
            @Query("country") String countries,
            @Query("category") String topic
    );

    @GET("api/1/news")
    Call<ResultModel> getLastestHeadlineNoTopic(

            @Query("apikey") String apikey,
            @Query("country") String countries
    );


}