package com.innocito.axcl.model;

import lombok.Data;

import java.util.List;

@Data
public class RequestedPageDetails {
    private Integer pageNumber;
    private Integer pageSize;
    private List<ErrorDetails> errors;
}
