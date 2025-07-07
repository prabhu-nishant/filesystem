package com.ppt.filesystem.controller;

import com.ppt.filesystem.model.*;
import com.ppt.filesystem.service.FileSystemService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

class FileSystemControllerTest {

    @Nested
    @ExtendWith(MockitoExtension.class)
    class FileSystemControllerUnitTest {
        @Mock
        private FileSystemService fileSystemService;

        @InjectMocks
        private FileSystemController target;

        @Captor
        private ArgumentCaptor<CreateFileRequest> createFileRequestArgumentCaptor;

        @Captor
        private ArgumentCaptor<DeleteFileRequest> deleteFileRequestArgumentCaptor;

        @Captor
        private ArgumentCaptor<MoveFileRequest> moveFileRequestArgumentCaptor;

        @Captor
        private ArgumentCaptor<WriteToFileRequest> writeToFileRequestArgumentCaptor;

        @Captor
        private ArgumentCaptor<PrintFileContentRequest> printFileContentRequestArgumentCaptor;

        @Test
        void createFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "testCreateFile", "\\");
            doNothing().when(fileSystemService).createFile(any(CreateFileRequest.class));
            target.createFile(createFileRequest);
            verify(fileSystemService).createFile(createFileRequestArgumentCaptor.capture());
            assertThat(createFileRequestArgumentCaptor.getValue()).isEqualTo(createFileRequest);
        }

        @Test
        void deleteFile() {
            var deleteFileRequest = new DeleteFileRequest("D\\C\\E");
            doNothing().when(fileSystemService).deleteFile(any(DeleteFileRequest.class));
            target.deleteFile(deleteFileRequest);
            verify(fileSystemService).deleteFile(deleteFileRequestArgumentCaptor.capture());
            assertThat(deleteFileRequestArgumentCaptor.getValue()).isEqualTo(deleteFileRequest);
        }

        @Test
        void moveFile() {
            var moveFileRequest = new MoveFileRequest("D\\C\\E", "A");
            doNothing().when(fileSystemService).moveFile(any(MoveFileRequest.class));
            target.moveFile(moveFileRequest);
            verify(fileSystemService).moveFile(moveFileRequestArgumentCaptor.capture());
            assertThat(moveFileRequestArgumentCaptor.getValue()).isEqualTo(moveFileRequest);
        }

        @Test
        void writeToFile() {
            var writeToFileRequest = new WriteToFileRequest("D\\C\\E", "Hello world!");
            doNothing().when(fileSystemService).writeToFile(any(WriteToFileRequest.class));
            target.writeToFile(writeToFileRequest);
            verify(fileSystemService).writeToFile(writeToFileRequestArgumentCaptor.capture());
            assertThat(writeToFileRequestArgumentCaptor.getValue()).isEqualTo(writeToFileRequest);
        }

        @Test
        void printFileContent() {
            var printFileContentRequest = new PrintFileContentRequest("D\\C\\E");
            var expectedContent = "Hello world!";
            when(fileSystemService.printFileContent(any(PrintFileContentRequest.class))).thenReturn("Hello world!");
            var content = target.printFileContent(printFileContentRequest);
            verify(fileSystemService).printFileContent(printFileContentRequestArgumentCaptor.capture());
            assertThat(printFileContentRequestArgumentCaptor.getValue()).isEqualTo(printFileContentRequest);
            assertThat(content.equals(expectedContent));
        }

        @Test
        void print() {
            doNothing().when(fileSystemService).printFileSystem();
            target.print();
            verify(fileSystemService, times(1)).printFileSystem();
        }
    }

    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    class FileSystemControllerIT{

        @Autowired
        private MockMvc mockMvc;

    }
}

