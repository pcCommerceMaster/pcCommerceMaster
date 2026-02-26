package com.pcproject.customer.dto;

import lombok.Getter;

import java.util.List;

@Getter

public class GetCustomerListResponse {
    private final List<GetCustomerResponse> customers;
    private final int currentPage;
    private final long totalElements;
    private final int totalPages;

    public GetCustomerListResponse(List<GetCustomerResponse> customers, int currentPage, long totalElements, int totalPages) {
        this.customers = customers;
        this.currentPage = currentPage;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
