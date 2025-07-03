package com.ppt.filesystem.exception;

import java.util.Map;

public class PathExistsException extends RuntimeException{

    private final Map<String, String> errorCodeMap;

    public PathExistsException(String errorMessage, Map<String, String> errorCodeMap) {
        super(errorMessage);
        this.errorCodeMap = errorCodeMap;
    }
}
