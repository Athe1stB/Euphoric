package com.example.euphoric.models;

import java.io.Serializable;

public class SpotifySong implements Serializable {

    private String id;
    private String name;
    private String uri;
    private String artist;
    private String album;

    public SpotifySong(String id, String name, String uri, String artist, String album) {
        this.name = name;
        this.id = id;
        this.uri = uri;
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
}
