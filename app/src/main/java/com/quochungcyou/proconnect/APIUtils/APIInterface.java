package com.quochungcyou.proconnect.APIUtils;


import com.quochungcyou.proconnect.Model.ResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("everything")
    Call<ResultModel> getNews(

            @Query("q") String q,
            @Query("sortBy") String sortby,
            @Query("apiKey") String apiKey
    );


}