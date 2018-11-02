package com.company.ui;

import com.company.entities.*;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Class for generating HTML files.
 */
public class HtmlGenerator {
    private final String ROW_H1_BEGIN = "<p><h1>";
    private final String ROW_H1_END = "</h1></p>";
    private final String ROW_H2_BEGIN = "<p><h2>";
    private final String ROW_H2_END = "</h2></p>";
    private final String ROW_P_BEGIN = "<p>";
    private final String ROW_P_END = "</p>";
    private final String ROW_END = "</a><br>";
    private final String FILE_LINK_PREFIX = "<a href=\"file:///";
    private final String FILE_LINK_POSTFIX = "\">";
    private final String DELIMITER = "<hr>";
    private final String SPACER = " - ";
    private final String LINK_TEXT = "Открыть файл";

    public HtmlGenerator() {
    }

    /**
     * Method creates and writes HTML file to the project directory.
     * @param fileName name of the HTML file.
     * @param html previosly generated HTML code.
     * @throws IOException when file write operation failed.
     */
    public void writeHtmlFile(String fileName, String html) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(html);
        writer.flush();
        writer.close();
    }

    /**
     * Generates HTML code for the given artists.
     * @param artists list of artists.
     * @return HTML code for the list of given artists
     */
    public String generateCollection(ArrayList<Artist> artists){
        StringBuilder document = new StringBuilder();
        for (Artist artist : artists){
            document.append(generateArtistSection(artist));
        }
        return document.toString();
    }

    /**
     * Generates HTML code for the duplicates, found by name.
     * @param duplicatedItems collection of duplicate items.
     * @return HTML code for the duplicates, found by name.
     * @throws InvalidDataException if the MP3 file is corrupted.
     * @throws IOException if the file read operation failed.
     * @throws UnsupportedTagException if the file has tag of unsupported format.
     */
    public String generateDuplicatesByName(ArrayList<DuplicatedItem> duplicatedItems) throws InvalidDataException,
            IOException, UnsupportedTagException {
        StringBuilder generatedHtml = new StringBuilder();
        for (DuplicatedItem item : duplicatedItems){
            generatedHtml.append(generateNameRow(item));
        }
        generatedHtml.append(DELIMITER);
        return generatedHtml.toString();
    }

    /**
     * Generates HTML code for the duplicates, found by MD5 hash.
     * @param duplicatedItems collection of duplicate items.
     * @return HTML code for the duplicates, found by by MD5 hash.
     */
    public String generateDuplicatesByHash(ArrayList<DuplicatedItem> duplicatedItems) {
        StringBuilder generatedHtml = new StringBuilder();
        for (DuplicatedItem item : duplicatedItems){
            generatedHtml.append(generateHashRow(item));
        }
        generatedHtml.append(DELIMITER);
        return generatedHtml.toString();
    }

    private String generateHashRow(DuplicatedItem item){
        StringBuilder hashRow = new StringBuilder(ROW_H2_BEGIN)
                .append(item.getComposedFilepath())
                .append(ROW_H2_END);
        for (String duplicatePath : item.getDuplicateFilePathes()){
            hashRow.append(ROW_P_BEGIN).append(duplicatePath).append(ROW_P_END);
        }
        return hashRow.toString();
    }

    private String generateNameRow(DuplicatedItem item) throws InvalidDataException,
            IOException, UnsupportedTagException {
        WrappedMp3File wrappedMp3File = new WrappedMp3File(item.getOriginalFilepath());
        StringBuilder row = new StringBuilder(ROW_H2_BEGIN)
                .append(wrappedMp3File.toString())
                .append(ROW_H2_END);
        for (String duplicatePath : item.getDuplicateFilePathes()){
            row.append(ROW_P_BEGIN).append(duplicatePath).append(ROW_P_END);
        }
        return row.toString();
    }

    private String generateArtistSection(Artist artist){
        StringBuilder artistSection = new StringBuilder(ROW_H1_BEGIN + artist.getArtistName() + ROW_H1_END);
        for (Album album : artist.getArtistAlbums()){
            artistSection.append(generateAlbumSection(album));
        }
        artistSection.append(DELIMITER);
        return artistSection.toString();
    }

    private String generateAlbumSection(Album album){
        StringBuilder albumSection = new StringBuilder(ROW_H2_BEGIN + album.getAlbumName() + ROW_H2_END);
        for (Song song : album.getAlbumSongs()){
            albumSection.append(generateSongRow(song));
        }
        albumSection.append(DELIMITER);
        return albumSection.toString();
    }

    private String generateSongRow(Song song){
        StringBuilder songRow = new StringBuilder();

        songRow.append(song.getSongArtist()).append(SPACER).append(song.getSongName()).append(SPACER)
                .append(formatDuration(song.getSongDuration())).append(SPACER)
                .append(FILE_LINK_PREFIX).append(song.getSongUri()).append(FILE_LINK_POSTFIX)
                .append(LINK_TEXT).append(ROW_END);
        return songRow.toString();
    }

    private String formatDuration(Long millis){
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}