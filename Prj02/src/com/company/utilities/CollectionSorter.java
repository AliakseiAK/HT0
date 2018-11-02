package com.company.utilities;

import com.company.entities.Album;
import com.company.entities.Artist;
import com.company.entities.Song;
import com.company.entities.WrappedMp3File;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Class scans directories and provides Albums and Songs lists.
 */
public class CollectionSorter {

    public CollectionSorter() {
    }

    /**
     * Method for sorting collection and preparing the list of Artist objects.
     * @return ArrayList of Artist objects that contain all albums that belong to them according to tracks MP3Id tags data.
     * @throws IOException in case of filesystem error.
     * @throws InvalidDataException in case of corrupted MP3 file.
     * @throws UnsupportedTagException in case of tags that are not valid Mp3Id v1/v2 format.
     */
    public ArrayList<Artist> generateCollection(ArrayList<Mp3File> mp3Files) throws IOException, InvalidDataException,
            UnsupportedTagException {
        ArrayList<Song> allSongs = convertMp3ToSongs(mp3Files);
        ArrayList<Album> albums = getAllAlbums(allSongs);
        ArrayList<Artist> artists = new ArrayList<>();

        HashMap<String, Album> uniqueArtists = new HashMap<>();
        for (Album album : albums){
            uniqueArtists.put(album.getAlbumArtist(), album);
        }
        for (String key : uniqueArtists.keySet()){
            Artist artist = new Artist();
            artist.setArtistName(uniqueArtists.get(key).getAlbumArtist());
            ArrayList<Album> artistAlbums = new ArrayList<>();
            for (Album album : albums){
                if (album.getAlbumArtist().equals(key)){
                    artistAlbums.add(album);
                }
            }
            artist.setArtistAlbums(artistAlbums);
            artists.add(artist);
        }
        return artists;
    }

    /**
     * Convert Mp3File to Song object
     * @param mp3FilePath path of file to convert.
     * @return Song object with proper values taken from MP3Id v1 and v2 tag.
     * If there are no data in tag, or if the tag is corrupted, or have unsupported format, "unknown artist",
     * "unknown album" and "unknown track" values are used.
     */
    private Song getSongFromMp3(String mp3FilePath) throws InvalidDataException, IOException, UnsupportedTagException {
        WrappedMp3File wrappedMp3File = new WrappedMp3File(mp3FilePath);

        return new Song(wrappedMp3File.getTitle(), wrappedMp3File.getLengthInMilliseconds(),
                wrappedMp3File.getFilename(), wrappedMp3File.getArtist(), wrappedMp3File.getAlbum());
    }

    /**
     * Converts ArrayList of Mp3files to ArrayList of Song objects.
     * @param mp3Files list of files to convert
     * @return list os Songs objects
     */
    private ArrayList<Song> convertMp3ToSongs(ArrayList<Mp3File> mp3Files) throws InvalidDataException, IOException, UnsupportedTagException {
        ArrayList<Song> songs = new ArrayList<>();
        for (Mp3File mp3File : mp3Files){
            songs.add(getSongFromMp3(mp3File.getFilename()));
        }
        return songs;
    }

    /**
     * As there can be albums with the same names from different artists, compound key is used.
     * @param songs list of Song objects.
     * @return list of Album objects that contain all the tracks of one album according to MP3Id data.
     */
    private ArrayList<Album>getAllAlbums(ArrayList<Song> songs){
        ArrayList<Album> albums = new ArrayList<>();
        HashMap<String, Song> songsByAlbums = new HashMap<>();
        for (Song song : songs){
            songsByAlbums.put(song.getSongArtist() + song.getSongAlbum(), song);
        }
        for (String key : songsByAlbums.keySet()){
            Album album = new Album();
            album.setAlbumName(songsByAlbums.get(key).getSongAlbum());
            album.setAlbumArtist(songsByAlbums.get(key).getSongArtist());
            ArrayList<Song> albumSongs = new ArrayList<>();
            for (Song song : songs){
                if ((song.getSongArtist()+song.getSongAlbum()).equals(key)){
                    albumSongs.add(song);
                }
            }
            album.setAlbumSongs(albumSongs);
            albums.add(album);
        }
        return albums;
    }
}