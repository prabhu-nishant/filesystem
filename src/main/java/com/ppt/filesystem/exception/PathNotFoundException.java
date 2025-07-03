package com.ppt.filesystem.exception;

import java.util.Map;

public class PathNotFoundException extends RuntimeException{

    private final Map<String, String> errorCodeMap;

    public PathNotFoundException(String errorMessage, Map<String, String> errorCodeMap) {
        super(errorMessage);
        this.errorCodeMap = errorCodeMap;
    }
}
