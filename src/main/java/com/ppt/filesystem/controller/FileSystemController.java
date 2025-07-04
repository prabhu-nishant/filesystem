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
        fileSystemService.createFile(new File(createFileRequest.fileType(), createFileRequest.name().trim(),
                createFileRequest.path().trim(), ""));
        return new FileSystemResponse("");
    }

    @PostMapping(value = "/delete")
    @ResponseStatus(OK)
    public FileSystemResponse deleteFile(@RequestBody @Valid DeleteFileRequest deleteFileRequest){
        fileSystemService.deleteFile(deleteFileRequest.path().trim());
        return new FileSystemResponse("");
    }

    @PostMapping(value = "/move")
    @ResponseStatus(OK)
    public FileSystemResponse moveFile(@RequestBody @Valid MoveFileRequest moveFileRequest){

        return new FileSystemResponse("");
    }

    @PostMapping(value = "/write-to-file")
    @ResponseStatus(OK)
    public FileSystemResponse writeToFile(@RequestBody @Valid WriteToFileRequest writeToFileRequest){

        return new FileSystemResponse("");
    }

    @GetMapping(value = "/display")
    @ResponseStatus(OK)
    public FileSystemResponse display(){
        fileSystemService.displayFileSystem();
        return new FileSystemResponse("");
    }
}
