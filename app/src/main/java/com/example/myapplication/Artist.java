package com.example.myapplication;
/**
 * Artist Class Hold the Artist Information Like artistName
 * ArtistId, songId and SongTitle
 * alog with getter and setter
 */
public class Artist {
    private String artistName;
    private String artistId;
    private String songId;
    private String songTitle;
    private  Long id;

    //chaining constructor
    public Artist(){
        this("","","","");
    }

    public Artist(String artistName, String artistId, String songId, String songTitle) {
        this(0L,artistName,artistId,songId,songTitle);

    }

    public Artist(Long id,String artistName, String artistId, String songId, String songTitle){
        this.id=id;
        this.artistName=artistName;
        this.artistId=artistId;
        this.songId=songId;
        this.songTitle=songTitle;




    }

    /*
     *getter and setter
     */

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
