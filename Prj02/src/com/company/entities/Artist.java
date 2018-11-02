package com.company.entities;

import java.util.ArrayList;

/**
 * Class for artist entities.
 */
public class Artist {
    private String artistName;
    private ArrayList<Album> artistAlbums;

    public Artist() {
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public ArrayList<Album> getArtistAlbums() {
        return artistAlbums;
    }

    public void setArtistAlbums(ArrayList<Album> artistAlbums) {
        this.artistAlbums = artistAlbums;
    }
}
