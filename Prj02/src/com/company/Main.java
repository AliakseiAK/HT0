package com.company;

import com.company.entities.Artist;
import com.company.entities.DuplicatedItem;
import com.company.utilities.CollectionSorter;
import com.company.utilities.DuplicatesFinder;
import com.company.ui.HtmlGenerator;
import com.company.utilities.FilesFinder;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, InvalidDataException,
            UnsupportedTagException, NoSuchAlgorithmException {
        System.out.println("You have entered " + args.length + " paths.");

        FilesFinder filesFinder = new FilesFinder(args); // dirs for test
        System.out.println("Searching for MP3 files...");
        ArrayList<Mp3File> mp3Files = filesFinder.find();
        System.out.println(mp3Files.size() + " MP3 files found.");
        System.out.println("Sorting collection...");
        CollectionSorter collectionSorter = new CollectionSorter();
        DuplicatesFinder duplicatesFinder = new DuplicatesFinder();
        HtmlGenerator htmlGenerator = new HtmlGenerator();

        String mp3Catalog = getMp3Catalog(mp3Files, collectionSorter, htmlGenerator);
        String duplicatesByName = getDuplicatesByName(mp3Files, duplicatesFinder, htmlGenerator);
        String duplicatesByHash = getDuplicatesByHash(mp3Files, duplicatesFinder, htmlGenerator);

        htmlGenerator.writeHtmlFile("mp3catalog.html", mp3Catalog);
        System.out.println("MP3 collection description succesfully generated.");

        htmlGenerator.writeHtmlFile("duplicatesByName.html", duplicatesByName);
        System.out.println("List of duplicates found by artist name, album name and song title succesfully generated.");

        htmlGenerator.writeHtmlFile("duplicatesByHash.html", duplicatesByHash);
        System.out.println("List of duplicates found by MD5 hash succesfully generated.");
        System.out.println("Waiting for Log4j2...");
    }

    private static String getMp3Catalog(ArrayList<Mp3File> mp3Files, CollectionSorter collectionSorter,
                                        HtmlGenerator htmlGenerator)
            throws IOException, InvalidDataException, UnsupportedTagException {
        ArrayList<Artist> artists = collectionSorter.generateCollection(mp3Files);
        return htmlGenerator.generateCollection(artists);
    }

    private static String getDuplicatesByName(ArrayList<Mp3File> mp3Files, DuplicatesFinder duplicatesFinder,
                                              HtmlGenerator htmlGenerator)
            throws InvalidDataException, IOException, UnsupportedTagException, NoSuchAlgorithmException {
        ArrayList<DuplicatedItem> duplicatedItems = duplicatesFinder.findByName(mp3Files);
        return htmlGenerator.generateDuplicatesByName(duplicatedItems);
    }

    private static String getDuplicatesByHash(ArrayList<Mp3File> mp3Files, DuplicatesFinder duplicatesFinder,
                                              HtmlGenerator htmlGenerator){
        ArrayList<DuplicatedItem> duplicatedItems = duplicatesFinder.findByHash(mp3Files);
        return htmlGenerator.generateDuplicatesByHash(duplicatedItems);
    }
}
