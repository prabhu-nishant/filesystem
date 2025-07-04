package com.ppt.filesystem.service;

import com.ppt.filesystem.exception.IllegalFileSystemException;
import com.ppt.filesystem.exception.PathExistsException;
import com.ppt.filesystem.exception.PathNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String[]>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        var errorMessages = exception.getBindingResult().getAllErrors().stream()
                .collect(toMap(
                        error -> error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName(),
                        error -> error.getCodes() == null ? new String[0] : error.getCodes()));
                return new ResponseEntity<>(errorMessages, BAD_REQUEST);
    }

    @ExceptionHandler(PathNotFoundException.class)
    public ResponseEntity<Map<String, String[]>> handlePathNotFoundException(PathNotFoundException exception){
        return new ResponseEntity<>(exception.getErrorCodeMap().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().toArray(String[]::new))), BAD_REQUEST);
    }

    @ExceptionHandler(PathExistsException.class)
    public ResponseEntity<Map<String, String[]>> handlePathExistsException(PathExistsException exception){
        return new ResponseEntity<>(exception.getErrorCodeMap().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().toArray(String[]::new))), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalFileSystemException.class)
    public ResponseEntity<Map<String, String[]>> handleIllegalFileSystemException(IllegalFileSystemException exception){
        return new ResponseEntity<>(exception.getErrorCodeMap().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().toArray(String[]::new))), BAD_REQUEST);
    }
}
