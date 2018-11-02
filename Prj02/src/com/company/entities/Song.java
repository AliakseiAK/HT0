package com.company.entities;

/**
 * Class for songs. Holds the song's text description.
 */
public class Song {
    private String songName;
    private String songUri;
    private String songArtist;
    private String songAlbum;
    private long songDuration;

    public Song(String songName, long songDuration, String songUri, String songArtist, String songAlbum) {
        this.songName = songName;
        this.songDuration = songDuration;
        this.songUri = songUri;
        this.songArtist = songArtist;
        this.songAlbum = songAlbum;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongUri() {
        return songUri;
    }

    public long getSongDuration() {
        return songDuration;
    }
}
