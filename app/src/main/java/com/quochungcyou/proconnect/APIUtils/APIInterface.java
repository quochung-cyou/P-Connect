package com.quochungcyou.proconnect.APIUtils;


import com.quochungcyou.proconnect.Model.ResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface {


    @GET("v2/latest_headlines")
    @Headers({
            "x-api-key: jYQA6FU8H_1-apVX4JNv6Yog7dMOVAoQXpY8aUhdjOk",
    })
    Call<ResultModel> getLastestHeadline(

            @Query("when") String when,
            @Query("countries") String countries,
            @Query("topic") String topic,
            @Query("page_size") String pageSize
    );

    @GET("v2/latest_headlines")
    @Headers({
            "x-api-key: jYQA6FU8H_1-apVX4JNv6Yog7dMOVAoQXpY8aUhdjOk",
    })
    Call<ResultModel> getLastestHeadlineNoTopic(

            @Query("when") String when,
            @Query("countries") String countries,
            @Query("page_size") String pageSize
    );


}