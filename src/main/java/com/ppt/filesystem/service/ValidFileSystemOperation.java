package com.ppt.filesystem.service;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FileSystemOperationValidator.class)
public @interface ValidFileSystemOperation {
    String message() default "Invalid File System Operation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
