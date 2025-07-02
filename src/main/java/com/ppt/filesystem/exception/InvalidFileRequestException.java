package com.ppt.filesystem.exception;

import java.util.List;
import java.util.Map;

public class InvalidFileRequestException extends RuntimeException{

    private final Map<String, List<String>> errorCodeMap;

    public InvalidFileRequestException(String errorMessage, Map<String, List<String>> errorCodeMap) {
        super(errorMessage);
        this.errorCodeMap = errorCodeMap;
    }
}
