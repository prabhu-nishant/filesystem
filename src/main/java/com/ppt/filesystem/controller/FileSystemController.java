package com.ppt.filesystem.controller;

import com.ppt.filesystem.model.*;
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

    @PostMapping(value = "/create")
    @ResponseStatus(CREATED)
    public FileSystemResponse createFile(@RequestBody @Valid CreateFileRequest createFileRequest){

        return new FileSystemResponse("");
    }

    @PostMapping(value = "/delete")
    @ResponseStatus(OK)
    public FileSystemResponse deleteFile(@RequestBody @Valid DeleteFileRequest deleteFileRequest){

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
}
