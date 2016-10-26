package com.github.ctaras.validation;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ValidationFieldError {
    private final String field;
    private final String message;
}
