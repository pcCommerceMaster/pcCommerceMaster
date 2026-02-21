package com.pcproject.order.dto;

import lombok.Getter;

@Getter
public class PageInfoResponse {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalCount;
}
