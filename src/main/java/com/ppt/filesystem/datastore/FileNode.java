package com.ppt.filesystem.datastore;

import com.ppt.filesystem.exception.IllegalFileSystemException;
import com.ppt.filesystem.exception.PathExistsException;
import com.ppt.filesystem.exception.PathNotFoundException;
import com.ppt.filesystem.model.File;
import com.ppt.filesystem.model.FileType;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
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
        var parentNode = getParentNode(this, newFile.path());
        validateInsertion(parentNode, newFile);
        parentNode.childNodes.putIfAbsent(newFile.name(), new FileNode(newFile));
    }

    public void delete(String deleteFilePath) {
        var parentNode = getParentNode(this, deleteFilePath);

        if (deleteFilePath.equals(parentNode.file.name())) {
            this.childNodes.remove(parentNode.file.name());
        }
        else {
            FileNode nodeToDelete = getChildNode(parentNode, deleteFilePath);
            parentNode.childNodes.remove(nodeToDelete.file.name());
        }
    }

    public void move(String sourcePath, String destinationPath) {

        var sourceNode = traverseNode(this, sourcePath);
        var destinationNode = traverseNode(this, destinationPath);
        var movedFileNode = getNewFileNode(sourceNode, destinationPath);
        validateInsertion(destinationNode, movedFileNode.file);
        updateChildNodes(movedFileNode.childNodes, movedFileNode.file.path());
        destinationNode.childNodes.putIfAbsent(movedFileNode.file.name(), movedFileNode);
        delete(sourcePath);
    }

    public void writeToFile(String filePath, String content) {

        FileNode nodeToBeUpdated = null;
        var parentNode = getParentNode(this, filePath);

        if (filePath.equals(parentNode.file.name())) {
            nodeToBeUpdated = parentNode;
        } else {
            nodeToBeUpdated = getChildNode(parentNode, filePath);
        }
        checkIfTextFile(filePath, nodeToBeUpdated);
        var file = nodeToBeUpdated.file;
        var newFile = new File(file.fileType(), file.name(), file.path(), content);
        var newFileNode = new FileNode(newFile);
        newFileNode.childNodes.putAll(nodeToBeUpdated.childNodes);
        parentNode.childNodes.replace(newFile.name(), newFileNode);
    }

    public String printFileContent(String path) {
        var node = traverseNode(this, path);
        return node.file.content();
    }

    public void print() {
        printNode(this, 0);
    }

    public void checkIfPathExists(FileNode parentNode, File newFile) {
        if (parentNode.childNodes.containsKey(newFile.name())) {
            throw new PathExistsException("File path already exists!", Map.of(PATH_ALREADY_EXISTS_ERROR_CODE,
                    List.of("Path already exists:" ,newFile.path())));
        }
    }

    public FileNode traverseNode(FileNode root, String path) {
        var pathSegments = path.split("\\\\");
        var currentNode = root;
        for (String segment : pathSegments) {
            currentNode = getChildNodeOrThrow(currentNode, segment);
        }
        return currentNode;
    }

    private FileNode getParentNode(FileNode root, String filePath) {
        var parentPath = extractParentPath(filePath);
        var parentNode = traverseNode(root, parentPath);
        return parentNode;
    }

    private FileNode getChildNode(FileNode parentNode, String filePath) {
        var fileName = extractFileName(filePath);
        var nodeToDelete = getChildNodeOrThrow(parentNode, fileName);
        return nodeToDelete;
    }

    private void updateChildNodes(Map<String, FileNode> childNodes, String destinationPath) {
        childNodes.replaceAll((name, childNode) -> {
            var updatedChild = getNewFileNode(childNode, destinationPath);
            updateChildNodes(updatedChild.childNodes, updatedChild.file.path());
            return updatedChild; });
    }

    private FileNode getNewFileNode(FileNode fileNode, String filePath){
        var file = fileNode.file;
        var filename = file.name();
        var newFilePath = filePath + "\\" + filename;
        var newFile = new File(file.fileType(), filename, newFilePath, file.content());
        var newFileNode = new FileNode(newFile);
        newFileNode.childNodes.putAll(fileNode.childNodes);
        return newFileNode;
    }

    private void printNode(FileNode fileNode, int level) {
        String indent = "\t".repeat(level);
        System.out.println(indent + " - " + fileNode.file.name() + " (" + fileNode.file.fileType() + ")" + " " + fileNode.file.path()    );

        for (FileNode child : fileNode.childNodes.values()) {
            printNode(child, level + 1);
        }
    }

    private String extractParentPath(String path) {
        int lastSeparatorIndex = path.lastIndexOf("\\");
        return (lastSeparatorIndex == -1) ? path : path.substring(0, lastSeparatorIndex);
    }

    private String extractFileName(String path) {
        int lastSeparatorIndex = path.lastIndexOf("\\");
        return (lastSeparatorIndex == -1) ? path : path.substring(lastSeparatorIndex + 1);
    }

    private FileNode getChildNodeOrThrow(FileNode parentNode, String fileName) {
        var childNode = parentNode.childNodes.get(fileName);
        if (childNode == null) {
            throw new PathNotFoundException("File path doesn't exist!",Map.of(PATH_NOT_FOUND_ERROR_CODE,
                    List.of("Path not found:" ,parentNode.file.path() + "\\" + fileName)));
        }
        return childNode;
    }

    private void validateInsertion(FileNode parentNode, File newFile) {
        checkIfPathExists(parentNode, newFile);
        checkIllegalFileSystemOperation(parentNode, newFile);
    }

    private void checkIfTextFile(String filePath, FileNode nodeToBeUpdated) {
        if(!FileType.TEXT_FILE.equals(nodeToBeUpdated.file.fileType())){
            throw new IllegalFileSystemException("Cannot write content to a non text file!!",
                    Map.of(ILLEGAL_FILE_SYSTEM_OPERATION_CODE, List.of("Cannot write content to a non text file!!",
                            filePath)));
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
