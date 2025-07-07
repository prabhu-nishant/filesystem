package com.ppt.filesystem.service;

import com.ppt.filesystem.datastore.FileNode;
import com.ppt.filesystem.model.File;
import com.ppt.filesystem.model.FileType;
import org.springframework.stereotype.Service;

@Service
public class FileSystemService {

    private final FileNode root = new FileNode(new File(FileType.DRIVE, "root", "\\root", ""));

    public void createFile(File createFile){
        root.insert(createFile);
    }

    public void deleteFile(String deleteFilePath){
        root.delete(deleteFilePath);
    }

    public void moveFile(String sourcePath, String destinationPath){
        root.move(sourcePath, destinationPath);
    }

    public void writeToFile(String filePath, String content) {
        root.writeToFile(filePath, content);
    }

    public String printFileContent(String path) { return root.printFileContent(path); }

    public void printFileSystem() {
        root.print();
    }
}
