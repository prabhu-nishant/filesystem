package com.ppt.filesystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        private ObjectMapper objectMapper;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private FileSystemService fileSystemService;

        @Test
        void createFile() throws Exception {
            var requestBody = toJsonString(new CreateFileRequest(FileType.DRIVE, "testCreateFile", "\\"));
            mockMvc.perform(post("/file/create")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(toJsonString(new FileSystemResponse("File Created successfully"))));
        }

        @Test
        void createFile_InvalidFileName() throws Exception {
            var requestBody = toJsonString(new CreateFileRequest(FileType.DRIVE, "", "A"));
            var mvcResult = mockMvc.perform(post("/file/create")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"name\":[\"NotBlank.createFileRequest.name\",\"NotBlank.name\",\"NotBlank.java.lang.String\",\"NotBlank\"]}"));
        }

        @Test
        void createFile_InvalidFileName2() throws Exception {
            var requestBody = toJsonString(new CreateFileRequest(FileType.DRIVE, "$%AF", "A"));
            var mvcResult = mockMvc.perform(post("/file/create")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"name\":[\"Pattern.createFileRequest.name\",\"Pattern.name\",\"Pattern.java.lang.String\",\"Pattern\"]}"));
        }

        @Test
        void createFile_InvalidPath() throws Exception {
            var requestBody = toJsonString(new CreateFileRequest(FileType.DRIVE, "A", ""));
            var mvcResult = mockMvc.perform(post("/file/create")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"path\":[\"Pattern.createFileRequest.path\",\"Pattern.path\",\"Pattern.java.lang.String\",\"Pattern\"]}"));
        }

        @Test
        void createFile_PathNotFound() throws Exception {
            var requestBody = toJsonString(new CreateFileRequest(FileType.DRIVE, "testCreateFile", "A"));
            var mvcResult = mockMvc.perform(post("/file/create")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"path_not_found_error_code\":[\"Path not found:\",\"\\root\\A\"]}"));
        }

        @Test
        void createFile_PathAlreadyExists() throws Exception {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            fileSystemService.createFile(createFileRequest);
            var requestBody = toJsonString(new CreateFileRequest(FileType.DRIVE, "A", "\\"));
            var mvcResult = mockMvc.perform(post("/file/create")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"path_already_exists_error_code\":[\"Path already exists:\",\"\\\\\\\\A\"]}"));
        }

        @Test
        void createFile_DriveUnderAnotherFile() throws Exception {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            fileSystemService.createFile(createFileRequest);
            var requestBody = toJsonString(new CreateFileRequest(FileType.DRIVE, "B", "A"));
            var mvcResult = mockMvc.perform(post("/file/create")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"illegal_file_system_operation_error_code\":[\"A drive cannot be created under another file!\",\"A\\\\B\"]}"));
        }

        @Test
        void deleteFile() throws Exception {
            var createFile = new CreateFileRequest(FileType.DRIVE, "D", "\\");
            fileSystemService.createFile(createFile);
            var requestBody = toJsonString(new DeleteFileRequest("D"));
            mockMvc.perform(post("/file/delete")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().json(toJsonString(
                            new FileSystemResponse("File has been deleted successfully"))));
        }

        @Test
        void deleteFile_PathNotFound() throws Exception {
            var requestBody = toJsonString(new DeleteFileRequest("D"));
            var mvcResult = mockMvc.perform(post("/file/delete")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"path_not_found_error_code\":[\"Path not found:\",\"\\root\\D\"]}"));
        }

        @Test
        void moveFile() throws Exception {
            var createFile = new CreateFileRequest(FileType.DRIVE, "D", "\\");
            var createFile1 = new CreateFileRequest(FileType.FOLDER, "E", "D");
            var createFile2 = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            fileSystemService.createFile(createFile);
            fileSystemService.createFile(createFile1);
            fileSystemService.createFile(createFile2);

            var requestBody = toJsonString(new MoveFileRequest("D\\E", "A"));
            mockMvc.perform(post("/file/move")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().json(toJsonString(
                            new FileSystemResponse("File has been moved successfully"))));
        }

        @Test
        void moveFile_PathNotFound() throws Exception {
            var createFile = new CreateFileRequest(FileType.DRIVE, "D", "\\");
            var createFile1 = new CreateFileRequest(FileType.FOLDER, "E", "D");
            var createFile2 = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            fileSystemService.createFile(createFile);
            fileSystemService.createFile(createFile1);
            fileSystemService.createFile(createFile2);

            var requestBody = toJsonString(new MoveFileRequest("D\\E\\F", "A"));
            var mvcResult = mockMvc.perform(post("/file/move")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"path_not_found_error_code\":[\"Path not found:\",\"\\root\\D\\E\\F\"]}"));
        }

        @Test
        void moveFile_PathAlreadyExists() throws Exception {
            var createFile = new CreateFileRequest(FileType.DRIVE, "D", "\\");
            var createFile1 = new CreateFileRequest(FileType.FOLDER, "E", "D");
            var createFile2 = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            var createFile3 = new CreateFileRequest(FileType.FOLDER, "E", "A");
            fileSystemService.createFile(createFile);
            fileSystemService.createFile(createFile1);
            fileSystemService.createFile(createFile2);
            fileSystemService.createFile(createFile3);

            var requestBody = toJsonString(new MoveFileRequest("D\\E", "A\\"));
            var mvcResult = mockMvc.perform(post("/file/move")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            var content = mvcResult.getResponse().getContentAsString();
            assertThat(content.equals("{\"path_already_exists_error_code\":[\"Path already exists:\",\"A\\\\\\\\E\"]}"));
        }

        @Test
        void writeToFile() throws Exception  {
            var createFile = new CreateFileRequest(FileType.DRIVE, "D", "\\");
            var createFile1 = new CreateFileRequest(FileType.TEXT_FILE, "I", "D");
            fileSystemService.createFile(createFile);
            fileSystemService.createFile(createFile1);

            var requestBody = toJsonString(new WriteToFileRequest("D\\I", "ABC"));
            mockMvc.perform(post("/file/write-to-file")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().json(toJsonString(
                            new FileSystemResponse("Content has been written to the file successfully"))));
        }

        @Test
        void printFileContent() throws Exception {
            var createFile = new CreateFileRequest(FileType.DRIVE, "D", "\\");
            var createFile1 = new CreateFileRequest(FileType.TEXT_FILE, "I", "D");
            var writeToFile = new WriteToFileRequest("D\\I", "ABC");
            fileSystemService.createFile(createFile);
            fileSystemService.createFile(createFile1);
            fileSystemService.writeToFile(writeToFile);

            var requestBody = toJsonString(new PrintFileContentRequest("D\\I"));
            mockMvc.perform(get("/file/print-file-content")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().json(toJsonString(new FileSystemResponse("ABC"))));
        }

        @Test
        void print() throws Exception {
            mockMvc.perform(get("/file/print")).andExpect(status().isOk());
        }

        private String toJsonString(Object data) throws JsonProcessingException {
            return objectMapper.writeValueAsString(data);
        }
    }
}

