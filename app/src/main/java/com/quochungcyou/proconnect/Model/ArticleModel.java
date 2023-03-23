package com.quochungcyou.proconnect.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleModel {
    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("link")
    @Expose
    private String url;

    @SerializedName("media")
    @Expose
    private String urlimage;

    @SerializedName("published_date")
    @Expose
    private String time;


    public String getSummary() {
        return summary;
    }
    

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}