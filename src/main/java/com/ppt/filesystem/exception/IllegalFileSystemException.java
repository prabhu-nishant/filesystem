package com.ppt.filesystem.exception;

import java.util.Map;

public class IllegalFileSystemException extends RuntimeException{

    private final Map<String, String> errorCodeMap;

    public IllegalFileSystemException(String errorMessage, Map<String, String> errorCodeMap) {
        super(errorMessage);
        this.errorCodeMap = errorCodeMap;
    }
}
