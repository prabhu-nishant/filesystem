package com.ppt.filesystem.controller;

import com.ppt.filesystem.model.*;
import com.ppt.filesystem.service.FileSystemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/file", produces = APPLICATION_JSON_VALUE)
public class FileSystemController {

    private final FileSystemService fileSystemService;

    @PostMapping(value = "/create")
    @ResponseStatus(CREATED)
    public FileSystemResponse createFile(@RequestBody @Valid CreateFileRequest createFileRequest){
        var fileType = createFileRequest.fileType();
        var name = createFileRequest.name().trim();
        var path = createFileRequest.path().trim() + "\\" + createFileRequest.name().trim();
        var content = "";
        var file = new File(fileType, name, path, content);
        fileSystemService.createFile(file);
        return new FileSystemResponse("File Created successfully");
    }

    @PostMapping(value = "/delete")
    @ResponseStatus(OK)
    public FileSystemResponse deleteFile(@RequestBody @Valid DeleteFileRequest deleteFileRequest){
        var deleteFilePath = deleteFileRequest.path().trim();
        fileSystemService.deleteFile(deleteFilePath);
        return new FileSystemResponse("File has been deleted successfully");
    }

    @PostMapping(value = "/move")
    @ResponseStatus(OK)
    public FileSystemResponse moveFile(@RequestBody @Valid MoveFileRequest moveFileRequest){
        var sourcePath = moveFileRequest.sourcePath().trim();
        var destinationPath = moveFileRequest.destinationPath().trim();
        fileSystemService.moveFile(sourcePath, destinationPath);
        return new FileSystemResponse("File has been deleted successfully");
    }

    @PostMapping(value = "/write-to-file")
    @ResponseStatus(OK)
    public FileSystemResponse writeToFile(@RequestBody @Valid WriteToFileRequest writeToFileRequest){

        return new FileSystemResponse("Content has been written to the file successfully");
    }

    @GetMapping(value = "/print")
    @ResponseStatus(OK)
    public FileSystemResponse print() {
        fileSystemService.printFileSystem();
        return new FileSystemResponse("");
    }
}
