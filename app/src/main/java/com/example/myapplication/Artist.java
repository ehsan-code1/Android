package com.example.myapplication;
public class Artist {
    protected String artistName;
    protected String artistId;
    protected String songId;
    protected String songTitle;
    protected String favouriteSong;
    protected Long id;

    public Artist() {

    }

    public Artist(String artistName, String artistId, String songId, String songTitle){
        this.artistName=artistName;
        this.artistId=artistId;
        this.songId=songId;
        //this.favouriteSong=favouriteSong;
        this.songTitle=songTitle;
        this.id=id;

    }

    public String getArtistName() {

        return artistName;
    }



    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {

        this.artistId = artistId;
    }

    public String getSongId() {

        return songId;
    }

    public void setSongId(String songId) {

        this.songId = songId;
    }

    public String getFavouriteSong() {

        return favouriteSong;
    }

    public void setFavouriteSong(String favouriteSong) {
        this.favouriteSong = favouriteSong;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
