package com.ppt.filesystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateFileRequest(

        @JsonProperty("fileType")
        @NotNull(message = "Invalid file type")
        FileType fileType,

        @JsonProperty("name")
        @NotNull(message = "Invalid file name")
        @NotBlank(message = "File name cannot be blank")
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Only Alphanumeric names allowed")
        String name,

        @JsonProperty("path")
        @NotNull(message = "Invalid path")
        @NotBlank(message = "Path cannot be blank")
        @Pattern(regexp = "^[A-Za-z0-9\\^]+$", message = "Only \\ can be used for concatenation of path names")
        String path) {
}
