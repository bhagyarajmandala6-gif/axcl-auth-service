package com.innocito.axcl.model;

import lombok.Data;

@Data
public class PaginationDetails {
    private int pageNumber;
    private int requestedPageSize;
    private long totalElements;
    private int totalPages;
    private int currentPageSize;
}