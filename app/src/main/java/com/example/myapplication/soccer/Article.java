package com.example.myapplication.soccer;

import android.graphics.drawable.Drawable;

public class Article {

    private String title;
    private String pubDate;
    private String url;
    private String description;
    private String imageUrl;
    private String thumbnailImageUrl;

    private Drawable image;
    private Drawable thumbnailImage;
    private boolean hasImage;
    private boolean hasThumbnailImage;

    private long id;

    public Article(String title, String pubDate, String url, String description, String imageUrl, String thumbnailImageUrl) {
        this.title = title;
        this.pubDate = pubDate;
        this.url = url;
        this.description = description;
        this.imageUrl = imageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public Article(String title, String pubDate, String url, String description, String imageUrl, String thumbnailImageUrl, long id) {
        this.title = title;
        this.pubDate = pubDate;
        this.url = url;
        this.description = description;
        this.imageUrl = imageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.id = id;
    }

    // getters and setters

    public void setId(long id) {
        this.id = id;
    }

    public void setImage(Drawable img) {
        this.image = img;
    }

    public void setThumbnailImage(Drawable thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public long getId() {
        return id;
    }

    public Drawable getImage() {
        return image;
    }

    public Drawable getThumbnailImage() {
        return thumbnailImage;
    }

    public boolean hasImage() {
        return (getImage() != null);
    }

    public boolean hasThumbnailImage() {
        return (getThumbnailImage() != null);
    }

}

