package com.pcproject.customer.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateCustomerStatusResponse {
    private final Long id;
    private final String status;
    private final LocalDateTime updatedAt;

    public UpdateCustomerStatusResponse(Long id, String status, LocalDateTime updatedAt) {
        this.id = id;
        this.status = status;
        this.updatedAt = updatedAt;
    }
}
