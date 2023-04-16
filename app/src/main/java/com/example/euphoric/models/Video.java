package com.example.euphoric.models;

import com.fasterxml.jackson.databind.JsonNode;

public class Video {
    public String id;
    public String channelId;
    public String title;
    public String channelTitle;
    public String description;
    public String thumbnail;
    public String publishedAt;

    public Video(JsonNode jsonNode){
        this.id = jsonNode.get("id").get("videoId").toString();
//        this.channelId = jsonNode.get("snippet").get("channelId").toString();
//        this.title = jsonNode.get("snippet").get("title").toString();
//        this.channelTitle = jsonNode.get("snippet").get("channelTitle").toString();
//        this.description = jsonNode.get("snippet").get("description").toString();
//        this.thumbnail = jsonNode.get("snippet").get("thumbnails").get("high").get("url").toString();
//        this.publishedAt = jsonNode.get("snippet").get("publishedAt").toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
