package com.ppt.filesystem.config;

import com.ppt.filesystem.datastore.FileNode;
import com.ppt.filesystem.model.File;
import com.ppt.filesystem.model.FileType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileSystemConfiguration {

    @Bean //Creates a root node
    public FileNode getRootNode(){
        return new FileNode(new File(FileType.DRIVE, "root", "\\root", ""));
    }
}
