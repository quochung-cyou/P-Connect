package com.quochungcyou.proconnect.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("total_hits")
    @Expose
    private int total_hits;

    @SerializedName("articles")
    @Expose
    private List<ArticleModel> articles;

    public String getStatus() {
        return status;
    }

    public int getTotalresults() {
        return total_hits;
    }

    public List<ArticleModel> getArticle() {
        return articles;
    }
}
