package com.ppt.filesystem.service;

import com.ppt.filesystem.datastore.FileNode;
import com.ppt.filesystem.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileSystemService {

    private final FileNode root;

    public void createFile(CreateFileRequest createFileRequest){
        var createFile = getFile(createFileRequest);
        root.insert(createFile);
    }

    public void deleteFile(DeleteFileRequest deleteFileRequest){
        var deleteFilePath = deleteFileRequest.path().trim();
        root.delete(deleteFilePath);
    }

    public void moveFile(MoveFileRequest moveFileRequest){
        var sourcePath = moveFileRequest.sourcePath().trim();
        var destinationPath = moveFileRequest.destinationPath().trim();
        root.move(sourcePath, destinationPath);
    }

    public void writeToFile(WriteToFileRequest writeToFileRequest) {
        var filePath = writeToFileRequest.path().trim();
        var content = writeToFileRequest.content().trim();
        root.writeToFile(filePath, content);
    }

    public String printFileContent(PrintFileContentRequest printFileContentRequest) {
        var path = printFileContentRequest.path();
        return root.printFileContent(path);
    }

    public void printFileSystem() {
        root.print();
    }

    private File getFile(CreateFileRequest createFileRequest) {
        var fileType = createFileRequest.fileType();
        var name = createFileRequest.name().trim();
        var path = createFileRequest.path().trim() + "\\" + createFileRequest.name().trim();
        var content = "";
        return new File(fileType, name, path, content);
    }
}
