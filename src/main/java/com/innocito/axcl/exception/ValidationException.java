package com.innocito.axcl.exception;

import com.innocito.axcl.model.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final List<ErrorDetails> errors;
}