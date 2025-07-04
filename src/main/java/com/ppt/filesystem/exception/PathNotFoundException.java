package com.ppt.filesystem.exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PathNotFoundException extends RuntimeException{

    private final Map<String, List<String>> errorCodeMap;

    public PathNotFoundException(String errorMessage, Map<String, List<String>> errorCodeMap) {
        super(errorMessage);
        this.errorCodeMap = errorCodeMap;
    }
}
