package com.example.euphoric.models;

import java.io.Serializable;

public class SpotifySong implements Serializable {

    private String id;
    private String name;
    private String uri;
    private String artist;
    private String album;
    private Long duration;
    private String thumbnail;

    public SpotifySong(String id, String name, String uri, String artist, String album, Long duration, String thumbnail) {
        this.name = name;
        this.id = id;
        this.uri = uri;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
