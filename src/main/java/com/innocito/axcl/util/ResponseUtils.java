package com.innocito.axcl.util;

import com.innocito.axcl.model.ApiResponse;
import com.innocito.axcl.model.ErrorDetails;
import com.innocito.axcl.model.PaginationDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResponseUtils {
    public <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data).build();
    }

    public <T> ApiResponse<T> success(T data, String message, PaginationDetails pagination) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .pagination(pagination).build();
    }

    public <T> ApiResponse<T> error(String message, List<ErrorDetails> errors) {
        return ApiResponse.<T>builder()
                .message(message)
                .errors(errors).build();
    }
}