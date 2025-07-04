package com.ppt.filesystem.datastore;

import com.ppt.filesystem.exception.IllegalFileSystemException;
import com.ppt.filesystem.exception.PathExistsException;
import com.ppt.filesystem.exception.PathNotFoundException;
import com.ppt.filesystem.model.File;
import com.ppt.filesystem.model.FileType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileNode {

    public static final String PATH_NOT_FOUND_ERROR_CODE = "path_not_found_error_code";
    public static final String PATH_ALREADY_EXISTS_ERROR_CODE = "path_already_exists_error_code";
    public static final String ILLEGAL_FILE_SYSTEM_OPERATION_CODE = "illegal_file_system_operation_error_code";
    private File file;
    private Map<String, FileNode> childNodes;

    public FileNode(File file) {
        this.file = file;
        childNodes = new ConcurrentHashMap<>();
    }

    public void insert(File newFile) {
        var parentNode = resolveParentNode(this, newFile.path());
        validateInsertion(parentNode, newFile);
        parentNode.childNodes.putIfAbsent(newFile.name(), new FileNode(newFile));
    }

    public void delete(String deleteFilePath) {
        var parentPath = extractParentPath(deleteFilePath);
        var parentNode = resolveParentNode(this, parentPath);

        if (deleteFilePath.equals(parentNode.file.name()+"\\")) {
            this.childNodes.remove(parentNode.file.name());
        }
        else {
            var fileName = extractFileName(deleteFilePath);
            var nodeToDelete = getChildNodeOrThrow(parentNode, fileName);
            parentNode.childNodes.remove(nodeToDelete.file.name());
        }
    }

    public void print() {
        printNode(this, 0);
    }

    private void printNode(FileNode fileNode, int level) {
        String indent = "\t".repeat(level);
        System.out.println(indent + "- " + fileNode.file.name() + " (" + fileNode.file.fileType() + ")");

        for (FileNode child : fileNode.childNodes.values()) {
            printNode(child, level + 1);
        }
    }

    private String extractParentPath(String path) {
        int lastSeparatorIndex = path.lastIndexOf("\\");
        return (lastSeparatorIndex == -1) ? "" : path.substring(0, lastSeparatorIndex);
    }

    private String extractFileName(String path) {
        int lastSeparatorIndex = path.lastIndexOf("\\");
        return (lastSeparatorIndex == -1) ? path : path.substring(lastSeparatorIndex + 1);
    }

    private FileNode resolveParentNode(FileNode root, String fullPath) {
        var pathSegments = fullPath.split("\\\\");
        var currentNode = root;
        for (String segment : pathSegments) {
            currentNode = getChildNodeOrThrow(currentNode, segment);
        }
        return currentNode;
    }

    private FileNode getChildNodeOrThrow(FileNode parentNode, String fileName) {
        var childNode = parentNode.childNodes.get(fileName);
        if (childNode == null) {
            throw new PathNotFoundException("File path doesn't exist!",Map.of(PATH_NOT_FOUND_ERROR_CODE,
                    List.of("Path not found:" ,parentNode.file.path() + fileName)));
        }
        return childNode;
    }

    private void validateInsertion(FileNode parentNode, File newFile) {
        checkIfPathExists(parentNode, newFile);
        checkIllegalFileSystemOperation(parentNode, newFile);
    }

    private void checkIfPathExists(FileNode parentNode, File newFile) {
        if (parentNode.childNodes.containsKey(newFile.name())) {
            throw new PathExistsException("File path already exists!", Map.of(PATH_ALREADY_EXISTS_ERROR_CODE,
                    List.of("Path already exists:" ,newFile.path() + newFile.name())));
        }
    }

    private void checkIllegalFileSystemOperation(FileNode parentNode, File newFile) {
        var parentType = parentNode.file.fileType();
        var newType = newFile.fileType();
        var newFilePath = newFile.path();

        if (parentType == FileType.TEXT_FILE) {
            throw new IllegalFileSystemException("A text file cannot contain another file!",
                    Map.of(ILLEGAL_FILE_SYSTEM_OPERATION_CODE, List.of("A text file cannot contain another file!",
                            newFilePath)));
        }

        if (newType == FileType.DRIVE && !parentNode.file.name().equals("root")) {
            throw new IllegalFileSystemException("A drive cannot be created under another file!",
                    Map.of(ILLEGAL_FILE_SYSTEM_OPERATION_CODE, List.of("A drive cannot be created under another file!",
                            newFilePath)));
        }

        if (newType != FileType.DRIVE && parentNode.file.name().equals("root")) {
            throw new IllegalFileSystemException("A non-drive file must be contained under another file!",
                    Map.of(ILLEGAL_FILE_SYSTEM_OPERATION_CODE,
                            List.of("A non-drive file must be contained under another file!", newFilePath)));
        }
    }
}
