package com.ppt.filesystem.model;

public record File(FileType fileType, String name, String path, String content) {
}
