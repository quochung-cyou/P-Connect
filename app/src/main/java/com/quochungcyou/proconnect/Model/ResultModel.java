package com.quochungcyou.proconnect.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalresults")
    @Expose
    private int totalresults;

    @SerializedName("articles")
    @Expose
    private List<ArticleModel> article;

    public String getStatus() {
        return status;
    }

    public int getTotalresults() {
        return totalresults;
    }

    public List<ArticleModel> getArticle() {
        return article;
    }
}
