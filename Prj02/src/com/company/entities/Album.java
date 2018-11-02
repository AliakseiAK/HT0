package com.company.entities;

import java.util.ArrayList;

/**
 * Class for album entities. Consists of album title, artist and list of songs.
 */
public class Album {
    private String albumName;
    private String albumArtist;
    private ArrayList<Song> albumSongs;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public ArrayList<Song> getAlbumSongs() {
        return albumSongs;
    }

    public void setAlbumSongs(ArrayList<Song> albumSongs) {
        this.albumSongs = albumSongs;
    }

    public Album(String albumName, String albumArtist, ArrayList<Song> albumSongs) {
        this.albumName = albumName;
        this.albumSongs = albumSongs;
        this.albumArtist = albumArtist;
    }

    public Album() {
    }
}
