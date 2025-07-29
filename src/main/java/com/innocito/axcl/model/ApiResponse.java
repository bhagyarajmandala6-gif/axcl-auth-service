package com.innocito.axcl.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiResponse<T> {
    private String message;
    private T data;
    private List<ErrorDetails> errors;
    private PaginationDetails pagination;
}