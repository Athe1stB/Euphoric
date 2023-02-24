package com.example.euphoric.models;

public class SongList {
    public String mSongName;
    public String[] mSongGenres;
    public String mArtist;
    public String mDuration;
    public String mAlbum;

    public SongList(String mSongName, String[] mSongGenres, String mArtist, String mDuration, String mAlbum) {
        this.mSongName = mSongName;
        this.mSongGenres = mSongGenres;
        this.mArtist = mArtist;
        this.mDuration = mDuration;
        this.mAlbum = mAlbum;
    }

    public String getmSongName() {
        return mSongName;
    }

    public void setmSongName(String mSongName) {
        this.mSongName = mSongName;
    }

    public String[] getmSongGenres() {
        return mSongGenres;
    }

    public void setmSongGenres(String[] mSongGenres) {
        this.mSongGenres = mSongGenres;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmAlbum() {
        return mAlbum;
    }

    public void setmAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }
}
