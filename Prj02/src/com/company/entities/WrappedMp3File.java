package com.company.entities;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

import static com.company.MyConstants.*;

/**
 * Wrapper for Mp3File class. Constructor checks for MP3 tag version and correctly initialises required fields.
 * Provides access to artist name, album title and song title.
 *
 * Another possible way to achieve goals of this class is to use Decorator pattern. As required superclass
 * fields are reachable and there is no need to inheritate from several classes simple extension was choosed.
 */
public class WrappedMp3File extends Mp3File {
    private String artist;
    private String album;
    private String title;

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public WrappedMp3File(String path) throws IOException, UnsupportedTagException, InvalidDataException {
        super(path);
        String title;
        String artist;
        String album;

        if (this.hasId3v2Tag()) {
            if (this.getId3v2Tag().getTitle() != null) {
                title = this.getId3v2Tag().getTitle();
            } else {
                title = UNKNOWN_TITLE;
            }
            if (this.getId3v2Tag().getAlbumArtist() != null) {
                artist = this.getId3v2Tag().getAlbumArtist();
            } else {
                artist = UNKNOWN_ARTIST;
            }
            if (this.getId3v2Tag().getAlbum() != null) {
                album = this.getId3v2Tag().getAlbum();
            } else {
                album = UNKNOWN_ALBUM;
            }
        } else if (this.hasId3v1Tag()) {
            if (this.getId3v1Tag().getTitle() != null) {
                title = this.getId3v1Tag().getTitle();
            } else {
                title = UNKNOWN_TITLE;
            }
            if (this.getId3v1Tag().getArtist() != null) {
                artist = this.getId3v1Tag().getArtist();
            } else {
                artist = UNKNOWN_ARTIST;
            }
            if (this.getId3v1Tag().getAlbum() != null) {
                album = this.getId3v1Tag().getAlbum();
            } else {
                album = UNKNOWN_ALBUM;
            }
        } else {
            title = UNKNOWN_TITLE;
            artist = UNKNOWN_ARTIST;
            album = UNKNOWN_ALBUM;
        }

        this.artist = artist;
        this.album = album;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Исполнитель " + this.artist
                + ", Альбом " + this.album
                + ", Композиция " + this.title;
    }
}