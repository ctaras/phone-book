package com.github.ctaras.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ValidationErrorBuilder {

    public static ValidationError fromBindingErrors(Errors errors) {
        ValidationError error = new ValidationError("Validation failed. " + errors.getErrorCount() + " error(s)");
        for (ObjectError objectError : errors.getAllErrors()) {
            if (objectError instanceof FieldError) {
                error.addValidationError(((FieldError) objectError).getField(), objectError.getDefaultMessage());
            } else {
                error.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
            }
        }
        return error;
    }
}