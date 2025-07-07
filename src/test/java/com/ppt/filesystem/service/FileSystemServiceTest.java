package com.ppt.filesystem.service;

import com.ppt.filesystem.datastore.FileNode;
import com.ppt.filesystem.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileSystemServiceTest {

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