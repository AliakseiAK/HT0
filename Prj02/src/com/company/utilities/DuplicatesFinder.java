package com.company.utilities;

import com.company.entities.DuplicatedItem;
import com.company.entities.WrappedMp3File;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DuplicatesFinder {
    private static final String NO_SUCH_ALGORITHM_EXCEPTION_MESSAGE = " NoSuchAlgorithmException in DuplicateFinder class. ";
    private static final String IOEXCEPTION_MESSAGE = " IOException in DuplicateFinder class. App cannot read the file: ";
    private static final String UNSUPPORDED_TAG_EXCEPTION_MESSAGE = " UnsupportedTagException in DuplicateFinder class. The tag is not a of a valid MP3IDv1 or MP3IDv2 type in file: ";
    private static final String INVALID_DATA_EXCEPTION_MESSAGE = " InvalidDataException in DuplicateFinder class. This file is probably not a valid MP3 file: ";
    private static final String UNSUPPORTED_ENCODING_EXCEPTION = " UnsupportedEncodingException in DuplicateFinder class ";

    private static final Marker MARKER_HASH = MarkerManager.getMarker("MARKER_HASH");
    private static final Marker MARKER_NAME = MarkerManager.getMarker("MARKER_NAME");
    private Logger logger = LogManager.getLogger(DuplicatesFinder.class);


    public DuplicatesFinder() {
        logger.info("New DuplicatesFinder object created.");
    }

    /**
     * Method finds all duplicates in given collection of MP3 files based on their names.
     * @param mp3Files given collection of MP3 files.
     * @return collection of grouped duplicate items.
     */
    public ArrayList<DuplicatedItem> findByName(ArrayList<Mp3File> mp3Files) {
        logger.info(MARKER_NAME,"Entered findByName(ArrayList<Mp3File> mp3Files) method of "
                + this.getClass().getName()
                + ". The size of mp3Files ArrayList is: " + mp3Files.size());
        ArrayList<DuplicatedItem> result = new ArrayList<>();
        List<String> filepaths = new ArrayList<>();
        for (Mp3File file : mp3Files){
            filepaths.add(file.getFilename());
        }
        HashMap<String, String> hashmap = new HashMap<>();
        HashMap<String, ArrayList<String>> packagedItemsPathHashMap = new HashMap<>();
        for(String currentFilepath : filepaths){
            WrappedMp3File wrappedMp3File = null;
            try {
                wrappedMp3File = new WrappedMp3File(currentFilepath);
            } catch (IOException e) {
                logger.error(MARKER_NAME, "Method findByName(ArrayList<Mp3File> mp3Files)." + IOEXCEPTION_MESSAGE + currentFilepath);
                e.printStackTrace();
            } catch (UnsupportedTagException e) {
                logger.error(MARKER_NAME,"Method findByName(ArrayList<Mp3File> mp3Files)." + UNSUPPORDED_TAG_EXCEPTION_MESSAGE + currentFilepath);
                e.printStackTrace();
            } catch (InvalidDataException e) {
                logger.error(MARKER_NAME,"Method findByName(ArrayList<Mp3File> mp3Files)." + INVALID_DATA_EXCEPTION_MESSAGE + currentFilepath);
                e.printStackTrace();
            }
            String fullName = wrappedMp3File.toString();
            if(hashmap.containsKey(fullName)){
                String pathToOriginal = hashmap.get(fullName);
                if(!packagedItemsPathHashMap.containsKey(pathToOriginal)){
                    ArrayList<String> duplicates = new ArrayList<>();
                    duplicates.add(currentFilepath);
                    packagedItemsPathHashMap.put(pathToOriginal, duplicates);
                    logger.info(MARKER_NAME,"New duplicate found for: " + pathToOriginal
                            + ". Path to duplicate: " + currentFilepath);
                }else{
                    ArrayList itemPaths = packagedItemsPathHashMap.get(pathToOriginal);
                    itemPaths.add(currentFilepath);
                    logger.info(MARKER_NAME,"New original file found: " + currentFilepath);
                }
            }else{
                hashmap.put(fullName, currentFilepath);
            }
        }
        result = generatePackagedItems(packagedItemsPathHashMap);
        logger.info(MARKER_NAME,"Generated " + result.size() + " groups of duplicate files.");
        return result;
    }

    /**
     * Method finds all duplicates in given collection of MP3 files based on their MD5 hashes.
     * More: https://stackoverflow.com/questions/40038729/check-duplicate-file-content-using-java
     * @param mp3Files given collection of MP3 files.
     * @return collection of grouped duplicate items.
     */
    public ArrayList<DuplicatedItem> findByHash(ArrayList<Mp3File> mp3Files) {
        logger.info(MARKER_HASH,"Entered findByHash(ArrayList<Mp3File> mp3Files) method of "
                + this.getClass().getName()
                + ". The size of mp3Files ArrayList is: " + mp3Files.size());
        ArrayList<DuplicatedItem> result;
        List<String> filepaths = new ArrayList<>();
        for (Mp3File file : mp3Files){
            filepaths.add(file.getFilename());
        }

        HashMap<String, String> keys = new HashMap<>(); // md5 keys, originalPathes
        HashMap<String, ArrayList<String>> packagedItemsPathHashMap = new HashMap<>(); //originalPath, PakagedItem
        for(String currentFilepath : filepaths){
            String md5 = getFileMD5(currentFilepath);
            if(keys.containsKey(md5)){
                String pathToOriginal = keys.get(md5);
                if(!packagedItemsPathHashMap.containsKey(pathToOriginal)){
                    ArrayList<String> duplicates = new ArrayList<>();
                    duplicates.add(currentFilepath);
                    packagedItemsPathHashMap.put(pathToOriginal, duplicates);
                    logger.info(MARKER_HASH,"New duplicate found for: " + pathToOriginal
                            + ". Path to duplicate: " + currentFilepath);
                }else{
                    ArrayList itemPaths = packagedItemsPathHashMap.get(pathToOriginal);
                    itemPaths.add(currentFilepath);
                    logger.info(MARKER_HASH,"New original file found: " + currentFilepath);
                }
            }else{
                keys.put(md5, currentFilepath);
            }
        }
        result = generatePackagedItems(packagedItemsPathHashMap);
        logger.info(MARKER_HASH,"Generated " + result.size() + " groups of duplicate files.");
        return result;
    }

    private ArrayList<DuplicatedItem> generatePackagedItems(HashMap<String, ArrayList<String>> map){
        logger.info(MARKER_HASH,"Entered findByHash(generatePackagedItems(HashMap<String, ArrayList<String>> map)" +
                " method of " + this.getClass().getName()
                + ". The size of HashMap map is: " + map.size());
        logger.info(MARKER_NAME,"Entered findByHash(generatePackagedItems(HashMap<String, ArrayList<String>> map)" +
                " method of " + this.getClass().getName()
                + ". The size of HashMap map is: " + map.size());
        ArrayList<DuplicatedItem> result = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()){
            DuplicatedItem duplicatedItem = new DuplicatedItem();
            duplicatedItem.setOriginalFilepath(entry.getKey());
            Mp3File currentMp3File = null;
            try {
                currentMp3File = new Mp3File(entry.getKey());
            } catch (IOException e) {
                logger.error(MARKER_HASH,"Method generatePackagedItems(HashMap<String, ArrayList<String>> map)"
                        + IOEXCEPTION_MESSAGE + entry.getKey());
                logger.error(MARKER_NAME,"Method generatePackagedItems(HashMap<String, ArrayList<String>> map)"
                        + IOEXCEPTION_MESSAGE + entry.getKey());
                e.printStackTrace();
            } catch (UnsupportedTagException e) {
                logger.error(MARKER_HASH, "Method generatePackagedItems(HashMap<String, ArrayList<String>> map)"
                        + UNSUPPORDED_TAG_EXCEPTION_MESSAGE + entry.getKey());
                logger.error(MARKER_NAME, "Method generatePackagedItems(HashMap<String, ArrayList<String>> map)"
                        + UNSUPPORDED_TAG_EXCEPTION_MESSAGE + entry.getKey());
                e.printStackTrace();
            } catch (InvalidDataException e) {
                logger.error(MARKER_HASH, "Method generatePackagedItems(HashMap<String, ArrayList<String>> map)"
                        + INVALID_DATA_EXCEPTION_MESSAGE + entry.getKey());
                logger.error(MARKER_NAME, "Method generatePackagedItems(HashMap<String, ArrayList<String>> map)"
                        + INVALID_DATA_EXCEPTION_MESSAGE + entry.getKey());
                e.printStackTrace();
            }
            duplicatedItem.setComposedFilepath(formatComposedFilepath(currentMp3File.getFilename()));
            duplicatedItem.setDuplicateFilePathes(entry.getValue());
            result.add(duplicatedItem);
            logger.info(MARKER_NAME, "Group of duplicates for " + duplicatedItem.getOriginalFilepath() + " file generated. "
                    + "Number of duplicates: " + duplicatedItem.getDuplicateFilePathes().size());
            logger.info(MARKER_HASH, "Group of duplicates for " + duplicatedItem.getOriginalFilepath() + " file generated. "
                    + "Number of duplicates: " + duplicatedItem.getDuplicateFilePathes().size());
        }
        return result;
    }

    private String formatComposedFilepath(String filePath) {
        logger.info("Entered formatComposedFilepath(String filePath) for file: " + filePath);
        WrappedMp3File wrappedMp3File= null;
        try {
            wrappedMp3File = new WrappedMp3File(filePath);
        } catch (IOException e) {
            logger.error(MARKER_NAME,"Method formatComposedFilepath(String filePath). " + IOEXCEPTION_MESSAGE + filePath);
            logger.error(MARKER_HASH,"Method formatComposedFilepath(String filePath). " + IOEXCEPTION_MESSAGE + filePath);
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            logger.error(MARKER_NAME, "Method formatComposedFilepath(String filePath). " + UNSUPPORDED_TAG_EXCEPTION_MESSAGE + filePath);
            logger.error(MARKER_HASH,"Method formatComposedFilepath(String filePath). " + UNSUPPORDED_TAG_EXCEPTION_MESSAGE + filePath);
            e.printStackTrace();
        } catch (InvalidDataException e) {
            logger.error(MARKER_NAME,"Method formatComposedFilepath(String filePath). " + INVALID_DATA_EXCEPTION_MESSAGE + filePath);
            logger.error(MARKER_HASH,"Method formatComposedFilepath(String filePath). " + INVALID_DATA_EXCEPTION_MESSAGE + filePath);
            e.printStackTrace();
        }
        return wrappedMp3File.getArtist() + " - " + wrappedMp3File.getAlbum() + " - " + wrappedMp3File.getTitle();
    }

    /**
     * This method generates MD5 hash for a given file.
     * More: https://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
     * More: https://javarevisited.blogspot.com/2013/03/generate-md5-hash-in-java-string-byte-array-example-tutorial.html
     * @param filePath path to the file.
     * @return MD5 hash.
     */
    private String getFileMD5(String filePath) {
        logger.info(MARKER_HASH,"Entered getFileMD5(String filePath) method for file: " + filePath);

        String digest = null;
        try {
            byte[] allBytes = Files.readAllBytes(Paths.get(filePath));
            byte[] hash = MessageDigest.getInstance("MD5").digest(allBytes);
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            logger.error(MARKER_HASH,  "Method getFileMD5(String filePath). " + UNSUPPORTED_ENCODING_EXCEPTION + ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(MARKER_HASH, "Method getFileMD5(String filePath). " + NO_SUCH_ALGORITHM_EXCEPTION_MESSAGE + ex);
        } catch (IOException e) {
            logger.error(MARKER_HASH, "Method getFileMD5(String filePath). " + IOEXCEPTION_MESSAGE + filePath);
            e.printStackTrace();
        }
        return digest;
    }
}
