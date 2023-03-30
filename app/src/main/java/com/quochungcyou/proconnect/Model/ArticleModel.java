package com.quochungcyou.proconnect.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleModel {
    @SerializedName("content")
    @Expose
    private String summary;

    @SerializedName("creator")
    @Expose
    private List<String> author;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("link")
    @Expose
    private String url;

    @SerializedName("image_url")
    @Expose
    private String urlimage;

    @SerializedName("pubDate")
    @Expose
    private String time;

    @SerializedName("source_id")
    @Expose
    private String source_id;


    public String getSummary() {
        return summary;
    }
    

    public List<String> getAuthor() {
        return author;
    }

    public String getSource_id() {
        return source_id;
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

    static class authorModel {
        @SerializedName("name")
        @Expose
        private String name;

    }
}