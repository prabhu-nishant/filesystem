package com.ppt.filesystem;

import com.ppt.filesystem.controller.FileSystemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class FileSystemApplicationIT {

    @Autowired
    private FileSystemController fileSystemController;

    @Test
    void contextLoads() { assertThat(fileSystemController).isNotNull(); }
}