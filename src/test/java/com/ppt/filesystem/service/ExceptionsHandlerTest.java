package com.ppt.filesystem.service;

import com.ppt.filesystem.datastore.FileNode;
import com.ppt.filesystem.exception.IllegalFileSystemException;
import com.ppt.filesystem.exception.PathExistsException;
import com.ppt.filesystem.exception.PathNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ExceptionsHandlerTest {

    @Test
    void handleMethodArgumentNotValidException() {
    }

    @Test
    void handlePathNotFoundException() {
        String expectedMessage = "File path doesn't exist!";
        Map<String, List<String>> errorCodeMap = Map.ofEntries(
                Map.entry(FileNode.PATH_NOT_FOUND_ERROR_CODE, List.of("Path not found:","C\\E")));

        var exception = new PathNotFoundException(expectedMessage, errorCodeMap);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
        assertThat(exception.getErrorCodeMap()).isEqualTo(errorCodeMap);
    }

    @Test
    void handlePathExistsException() {
        String expectedMessage = "File path already exists!";
        Map<String, List<String>> errorCodeMap = Map.ofEntries(
                Map.entry(FileNode.PATH_ALREADY_EXISTS_ERROR_CODE, List.of("Path already exists:","C\\E")));

        var exception = new PathExistsException(expectedMessage, errorCodeMap);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
        assertThat(exception.getErrorCodeMap()).isEqualTo(errorCodeMap);
    }

    @Test
    void handleIllegalFileSystemException() {
        String expectedMessage = "A text file cannot contain another file!";
        Map<String, List<String>> errorCodeMap = Map.ofEntries(
                Map.entry(FileNode.ILLEGAL_FILE_SYSTEM_OPERATION_CODE,
                        List.of("A text file cannot contain another file!","C\\E")));

        var exception = new IllegalFileSystemException(expectedMessage, errorCodeMap);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
        assertThat(exception.getErrorCodeMap()).isEqualTo(errorCodeMap);
    }
}