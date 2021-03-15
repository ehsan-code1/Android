package com.example.myapplication;
public class Artist {
    protected String artistName;
    protected long artistId;
    protected long songId;
    protected String favouriteSong;

    public Artist(String artistName,long artistId,long songId,String favouriteSong){
        this.artistName=artistName;
        this.artistId=artistId;
        this.songId=songId;
        this.favouriteSong=favouriteSong;

    }

    public String getArtistName() {

        return artistName;
    }

    public long getId() {
        return artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getFavouriteSong() {
        return favouriteSong;
    }

    public void setFavouriteSong(String favouriteSong) {
        this.favouriteSong = favouriteSong;
    }
}
