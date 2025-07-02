package com.ppt.filesystem.service;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PathValidator implements ConstraintValidator<ValidPath, String> {


    @Override
    public boolean isValid(String path, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
