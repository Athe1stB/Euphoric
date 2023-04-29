package com.example.euphoric.models;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class Video implements Serializable {
    public String id;
    public String channelId;
    public String title;
    public String channelTitle;
    public String description;
    public String thumbnail;
    public String publishedAt;

    private String getValid(String value){
        int length = value.length();
        return value.substring(1,length-1);
    }

    public Video(JsonNode jsonNode){
        this.id = getValid(jsonNode.get("id").get("videoId").toString());
        this.channelId = getValid(jsonNode.get("snippet").get("channelId").toString());
        this.title = getValid(jsonNode.get("snippet").get("title").toString());
        this.channelTitle = getValid(jsonNode.get("snippet").get("channelTitle").toString());
        this.description = getValid(jsonNode.get("snippet").get("description").toString());
        this.thumbnail = getValid(jsonNode.get("snippet").get("thumbnails").get("high").get("url").toString());
        this.publishedAt = getValid(jsonNode.get("snippet").get("publishedAt").toString());
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
