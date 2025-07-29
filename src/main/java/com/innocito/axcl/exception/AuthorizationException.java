package com.innocito.axcl.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String key;
    private final String message;
}