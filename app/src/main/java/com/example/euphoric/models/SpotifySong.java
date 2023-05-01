package com.example.euphoric.models;

import java.io.Serializable;

public class SpotifySong implements Serializable {

    private String id;
    private String name;
    private String href;
    private String artist;
    private String album;

    public SpotifySong(String id, String name, String href, String artist, String album) {
        this.name = name;
        this.id = id;
        this.href = href;
        this.artist = artist;
        this.album = album;
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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
}
