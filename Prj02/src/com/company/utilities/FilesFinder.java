package com.company.utilities;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.company.MyConstants.FORMAT_MP3;
import static com.company.MyConstants.WINDOWS_MAX_FOLDER_DEPTH;

public class FilesFinder {
    private ArrayList<File> directories = new ArrayList();

    public FilesFinder(String[] directories) {
        HashSet<String> directoriesToCheck = new HashSet<>(checkPathes(Arrays.asList(directories)));
        for (String dirPath : directoriesToCheck){
            if (isDir(dirPath)){
                this.directories.add(getDirFromPath(dirPath));
            }
        }
    }

    /**
     * Method generates full list of found mp3 files.
     * @return list of found mp3 files.
     * @throws UnsupportedTagException if the tag is not a MP3ID v1/2.
     * @throws InvalidDataException if the file is corrupted.
     * @throws IOException if the file is unreachable.
     */
    public ArrayList<Mp3File> find() throws UnsupportedTagException, InvalidDataException,
            IOException {
        ArrayList<Mp3File> allMp3files = new ArrayList<>();
        for (File directory : this.directories){
            allMp3files.addAll(getMp3fromDir(directory));
        }
        return allMp3files;
    }

    private List checkPathes(List directoriesToCheck) {
        for (String dirPath : (List<String>)directoriesToCheck){
            File file = new File(dirPath);
            if (!file.exists()) {
                try{
                    if (!directoriesToCheck.remove(dirPath)){
                        throw new UnsupportedOperationException();
                    }
                }catch (UnsupportedOperationException ex){
                    System.out.println(ex.toString());
                }
            }
        }
        return directoriesToCheck;
    }

    private File getDirFromPath(String path) {
        return new File(path);
    }

    private boolean isDir(String dirPath) {
        File file = new File(dirPath);
        return file.isDirectory();
    }

    /**
     * Method uses Java8 NIO as it correctly handles filesystem permissions.
     * Alternative solution is to use recursive algorithm.
     * @param directory path to a starting point.
     * @return list of mp3 files.
     * @throws IOException if the path is unreachable.
     */
    private ArrayList<Mp3File> getMp3fromDir(File directory) throws IOException, InvalidDataException, UnsupportedTagException {
        Path start = Paths.get(directory.getAbsolutePath());
        int maxDepth = WINDOWS_MAX_FOLDER_DEPTH;
        Stream<Path> files = Files.find(start, maxDepth, (path, basicFileAttributes)
                -> String.valueOf(path).endsWith(FORMAT_MP3));
        List<File> fileList = files.map(Path::toFile).collect(Collectors.toList());

        ArrayList<Mp3File> mp3Files = new ArrayList<>();
        for (File file : fileList){
            Mp3File mp3File = new Mp3File(file);
            mp3Files.add(mp3File);
        }
        return mp3Files;
    }
}