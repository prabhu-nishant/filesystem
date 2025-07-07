package com.ppt.filesystem.service;

import com.ppt.filesystem.datastore.FileNode;
import com.ppt.filesystem.exception.IllegalFileSystemException;
import com.ppt.filesystem.exception.PathExistsException;
import com.ppt.filesystem.exception.PathNotFoundException;
import com.ppt.filesystem.model.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

class FileSystemServiceTest {

    @Nested
    @ExtendWith(MockitoExtension.class)
    class FileSystemServiceUnitTest{
        @Mock
        private FileNode root;

        @InjectMocks
        private FileSystemService target;

        @Captor
        private ArgumentCaptor<File> fileArgumentCaptor;

        @Captor
        private ArgumentCaptor<String> stringArgumentCaptor;

        @Captor
        private ArgumentCaptor<String> stringArgumentCaptor2;

        @Test
        void createFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "testCreateFile", "\\");
            var expectedFile = new File(FileType.DRIVE, "testCreateFile", "\\\\testCreateFile", "");
            doNothing().when(root).insert(any(File.class));
            target.createFile(createFileRequest);
            verify(root).insert(fileArgumentCaptor.capture());
            assertThat(fileArgumentCaptor.getValue()).isEqualTo(expectedFile);
        }

        @Test
        void deleteFile() {
            var deleteFileRequest = new DeleteFileRequest("D\\C\\E");
            var expectedDeleteFilePath = "D\\C\\E";
            doNothing().when(root).delete(anyString());
            target.deleteFile(deleteFileRequest);
            verify(root).delete(stringArgumentCaptor.capture());
            assertThat(stringArgumentCaptor.getValue()).isEqualTo(expectedDeleteFilePath);
        }

        @Test
        void moveFile() {
            var moveFileRequest = new MoveFileRequest("D\\C\\E", "A");
            var expectedFilePath = "D\\C\\E";
            var expectedDestinationFilePath = "A";
            doNothing().when(root).move(anyString(), anyString());
            target.moveFile(moveFileRequest);
            verify(root).move(stringArgumentCaptor.capture(), stringArgumentCaptor2.capture());
            assertThat(stringArgumentCaptor.getValue()).isEqualTo(expectedFilePath);
            assertThat(stringArgumentCaptor2.getValue()).isEqualTo(expectedDestinationFilePath);
        }

        @Test
        void writeToFile() {
            var writeToFileRequest = new WriteToFileRequest("D\\C\\E", "Hello world!");
            var excpectedFilePath = "D\\C\\E";
            var expectedContent = "Hello world!";
            doNothing().when(root).writeToFile(anyString(), anyString());
            target.writeToFile(writeToFileRequest);
            verify(root).writeToFile(stringArgumentCaptor.capture(), stringArgumentCaptor2.capture());
            assertThat(stringArgumentCaptor.getValue()).isEqualTo(excpectedFilePath);
            assertThat(stringArgumentCaptor2.getValue()).isEqualTo(expectedContent);
        }

        @Test
        void printFileContent() {
            var printFileContentRequest = new PrintFileContentRequest("D\\C\\E");
            var expectedFilePath = "D\\C\\E";
            var expectedContent = "Hello world!";
            when(root.printFileContent(anyString())).thenReturn("Hello world!");
            var content = target.printFileContent(printFileContentRequest);
            verify(root).printFileContent(stringArgumentCaptor.capture());
            assertThat(stringArgumentCaptor.getValue()).isEqualTo(expectedFilePath);
            assertThat(content.equals(expectedContent));
        }

        @Test
        void printFileSystem() {
            doNothing().when(root).print();
            target.printFileSystem();
            verify(root, times(1)).print();
        }
    }

    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
    class FileSystemServiceIT{

        @Autowired
        private FileNode node;

        @Autowired
        private FileSystemService target;

        @Test
        void createFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            target.createFile(createFileRequest);
            var fileNode = node.traverseNode(node, "A");
            var file = fileNode.getFile();
            assertThat(file.fileType()).isEqualTo(FileType.DRIVE);
            assertThat(file.name()).isEqualTo("A");
            assertThat(file.path()).isEqualTo("\\\\A");
        }

        @Test
        void createFile_PathAlreadyExists() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            target.createFile(createFileRequest);
            var createFileRequest2 = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            var exception = assertThrows(PathExistsException.class, () -> {
                target.createFile(createFileRequest2);
            });
            assertThat(exception.getMessage().equals("File path already exists!"));
        }

        @Test
        void createFile_TextFileUnderAnotherTextFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            target.createFile(createFileRequest);
            var createFileRequest2 = new CreateFileRequest(FileType.TEXT_FILE, "B", "A");
            target.createFile(createFileRequest2);
            var createFileRequest3 = new CreateFileRequest(FileType.TEXT_FILE, "C", "A\\B");
            var exception = assertThrows(IllegalFileSystemException.class, () -> {
                target.createFile(createFileRequest3);
            });
            assertThat(exception.getMessage().equals("A text file cannot contain another file!"));
        }

        @Test
        void createFile_DriveUnderAnotherFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            target.createFile(createFileRequest);
            var createFileRequest2 = new CreateFileRequest(FileType.DRIVE, "B", "A");
            var exception = assertThrows(IllegalFileSystemException.class, () -> {
                target.createFile(createFileRequest2);
            });
            assertThat(exception.getMessage().equals("A drive cannot be created under another file!"));
        }

        @Test
        void createFile_NonDriveFileShouldBeUnderAnotherFile() {
            var createFileRequest = new CreateFileRequest(FileType.TEXT_FILE, "A", "\\");
            var exception = assertThrows(IllegalFileSystemException.class, () -> {
                target.createFile(createFileRequest);
            });
            assertThat(exception.getMessage().equals("A non-drive file must be contained under another file!"));
        }

        @Test
        void deleteFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            var deleteFileRequest = new DeleteFileRequest("A");
            target.createFile(createFileRequest);
            target.deleteFile(deleteFileRequest);
            var exception = assertThrows(PathNotFoundException.class, () -> {
                node.traverseNode(node, "A");
            });
            assertThat(exception.getMessage().equals("File path doesn't exist!"));
        }

        @Test
        void moveFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            var createFileRequest1 = new CreateFileRequest(FileType.FOLDER, "B", "A");
            var createFileRequest2 = new CreateFileRequest(FileType.DRIVE, "C", "\\");
            var moveFileRequest = new MoveFileRequest("A\\B", "C");
            target.createFile(createFileRequest);
            target.createFile(createFileRequest1);
            target.createFile(createFileRequest2);
            target.moveFile(moveFileRequest);

            var fileNode = node.traverseNode(node, "C\\B");
            var file = fileNode.getFile();
            assertThat(file.fileType()).isEqualTo(FileType.FOLDER);
            assertThat(file.name()).isEqualTo("B");
            assertThat(file.path()).isEqualTo("C\\B");

            var exception = assertThrows(PathNotFoundException.class, () -> {
                node.traverseNode(node, "A\\B");
            });
            assertThat(exception.getMessage().equals("File path doesn't exist!"));
        }

        @Test
        void writeToFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            var createFileRequest1 = new CreateFileRequest(FileType.TEXT_FILE, "B", "A");
            var writeToFileRequest = new WriteToFileRequest("A\\B", "Hello World!");
            target.createFile(createFileRequest);
            target.createFile(createFileRequest1);
            target.writeToFile(writeToFileRequest);

            var fileNode = node.traverseNode(node, "A\\B");
            var file = fileNode.getFile();
            assertThat(file.fileType()).isEqualTo(FileType.TEXT_FILE);
            assertThat(file.name()).isEqualTo("B");
            assertThat(file.path()).isEqualTo("A\\B");
            assertThat(file.content()).isEqualTo("Hello World!");
        }

        @Test
        void writeToNonTextFile() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            var writeToFileRequest = new WriteToFileRequest("A", "Hello World!");
            target.createFile(createFileRequest);
            var exception = assertThrows(IllegalFileSystemException.class, () -> {
                target.writeToFile(writeToFileRequest);
            });
            assertThat(exception.getMessage().equals("Cannot write content to a non text file!!"));
        }

        @Test
        void printFileContent() {
            var createFileRequest = new CreateFileRequest(FileType.DRIVE, "A", "\\");
            var createFileRequest1 = new CreateFileRequest(FileType.TEXT_FILE, "B", "A");
            var writeToFileRequest = new WriteToFileRequest("A\\B", "Hello World!");
            var printFileContentRequest = new PrintFileContentRequest("A\\B");
            target.createFile(createFileRequest);
            target.createFile(createFileRequest1);
            target.writeToFile(writeToFileRequest);
            var fileContent = target.printFileContent(printFileContentRequest);
            assertThat(fileContent).isEqualTo("Hello World!");
        }
    }
}