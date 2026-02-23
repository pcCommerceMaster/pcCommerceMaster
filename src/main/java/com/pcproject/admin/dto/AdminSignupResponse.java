package com.pcproject.admin.dto;

import com.pcproject.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminSignupResponse {

    private final Long id;
    private final AdminStatus status;
    private final LocalDateTime createdAt;

    public AdminSignupResponse(Long id, AdminStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
    }
}
