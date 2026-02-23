package com.pcproject.customer.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateCustomerInfoResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime updatedAt; // 수정 완료 시점 강조

    public UpdateCustomerInfoResponse(Long id, String name, String email, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.updatedAt = updatedAt;
    }
}
