package com.github.ctaras.validation;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {

    private final String errorMessage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationFieldError> errors = new ArrayList<>();

    public ValidationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addValidationError(String field, String error) {
        errors.add(new ValidationFieldError(field, error));
    }

    public List<ValidationFieldError> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}