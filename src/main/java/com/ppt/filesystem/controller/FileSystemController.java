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

    @PostMapping(value = "/create")  // Create a file
    @ResponseStatus(CREATED)
    public FileSystemResponse createFile(@RequestBody @Valid CreateFileRequest createFileRequest){
        fileSystemService.createFile(createFileRequest);
        return new FileSystemResponse("File Created successfully");
    }

    @PostMapping(value = "/delete") // Create a file
    @ResponseStatus(OK)
    public FileSystemResponse deleteFile(@RequestBody @Valid DeleteFileRequest deleteFileRequest){
        fileSystemService.deleteFile(deleteFileRequest);
        return new FileSystemResponse("File has been deleted successfully");
    }

    @PostMapping(value = "/move") // Move a file
    @ResponseStatus(OK)
    public FileSystemResponse moveFile(@RequestBody @Valid MoveFileRequest moveFileRequest){
        fileSystemService.moveFile(moveFileRequest);
        return new FileSystemResponse("File has been moved successfully");
    }

    @PostMapping(value = "/write-to-file") // Write to a file
    @ResponseStatus(OK)
    public FileSystemResponse writeToFile(@RequestBody @Valid WriteToFileRequest writeToFileRequest){
        fileSystemService.writeToFile(writeToFileRequest);
        return new FileSystemResponse("Content has been written to the file successfully");
    }

    @GetMapping(value = "/print-file-content") // Prints the contents of a file
    @ResponseStatus(OK)
    public FileSystemResponse printFileContent(@RequestBody @Valid PrintFileContentRequest printFileContentRequest) {
        var response = fileSystemService.printFileContent(printFileContentRequest);
        return new FileSystemResponse(response);
    }

    @GetMapping(value = "/print")  // Prints the entire file system on the system and does not print anything in the response
    @ResponseStatus(OK)
    public FileSystemResponse print() {
        fileSystemService.printFileSystem();
        return new FileSystemResponse("");
    }
}
