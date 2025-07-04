package com.ppt.filesystem.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum FileType {
    DRIVE("Drive"),
    FOLDER("Folder"),
    ZIP_FILE("Zip"),
    TEXT_FILE("Text");

    private final String value;

    @JsonCreator
    public static FileType fromValue(String value){
        return Arrays.stream(values())
                .filter(fileType -> fileType.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid file type: " + value));
    }

    @JsonValue
    public String getValue() { return value; }
}
