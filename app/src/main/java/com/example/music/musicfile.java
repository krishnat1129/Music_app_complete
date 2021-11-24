package com.example.music;

public class musicfile {
    private String path;
    private String album;
    private String title;
    private String artist;
    private String duration;
    private String date;

    public musicfile(String path, String album, String title, String artist, String duration,String date) {
        this.path = path;
        this.album = album;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.date = date;
    }

    public musicfile() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate(){return date; }

    public void setDate(String date){this.date = date;}
}
