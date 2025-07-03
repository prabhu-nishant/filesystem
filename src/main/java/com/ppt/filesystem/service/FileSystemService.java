package com.ppt.filesystem.service;

import com.ppt.filesystem.datastore.FileNode;
import com.ppt.filesystem.model.File;
import com.ppt.filesystem.model.FileType;
import org.springframework.stereotype.Service;

@Service
public class FileSystemService {

    private final FileNode root = new FileNode(new File(FileType.DRIVE, "root", "\\", ""));

    public void createFile(File createFile){
        root.insert(createFile);
    }

    public void deleteFile(String deleteFilePath){
        root.delete(deleteFilePath);
    }

}
