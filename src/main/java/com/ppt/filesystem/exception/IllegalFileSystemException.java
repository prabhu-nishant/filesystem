package com.ppt.filesystem.exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class IllegalFileSystemException extends RuntimeException{

    private final Map<String, List<String>> errorCodeMap;

    public IllegalFileSystemException(String errorMessage, Map<String, List<String>> errorCodeMap) {
        super(errorMessage);
        this.errorCodeMap = errorCodeMap;
    }
}
