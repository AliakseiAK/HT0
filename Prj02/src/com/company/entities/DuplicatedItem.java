package com.company.entities;

import java.util.ArrayList;

/**
 * Class for duplicates items.
 * Each item consists of of the unique original file's filepath and list of it's duplicates filepathes.
 */
public class DuplicatedItem {
    private String originalFilepath;
    private String composedFilepath;
    private ArrayList<String> duplicateFilePathes;

    public DuplicatedItem() {
    }

    public ArrayList<String> getDuplicateFilePathes() {
        return duplicateFilePathes;
    }

    public void setDuplicateFilePathes(ArrayList<String> duplicateFilePathes) {
        this.duplicateFilePathes = duplicateFilePathes;
    }

    public String getOriginalFilepath() {
        return originalFilepath;
    }

    public void setOriginalFilepath(String originalFilepath) {
        this.originalFilepath = originalFilepath;
    }

    public String getComposedFilepath() {
        return composedFilepath;
    }

    public void setComposedFilepath(String composedFilepath) {
        this.composedFilepath = composedFilepath;
    }
}
