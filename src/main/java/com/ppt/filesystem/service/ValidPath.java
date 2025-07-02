package com.ppt.filesystem.service;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PathValidator.class)
public @interface ValidPath {
    String message() default "Invalid path";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
