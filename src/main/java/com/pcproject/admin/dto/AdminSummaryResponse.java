package com.pcproject.admin.dto;

import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.entity.AdminRole;
import com.pcproject.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminSummaryResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final AdminRole role;
    private final AdminStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;

    public AdminSummaryResponse(Long id, String name, String email, String phoneNumber, AdminRole role, AdminStatus status, LocalDateTime createdAt, LocalDateTime approvedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }

    public static AdminSummaryResponse from (Admin a) {
        return new AdminSummaryResponse(
                a.getId(),
                a.getName(),
                a.getEmail(),
                a.getPhoneNumber(),
                a.getRole(),
                a.getStatus(),
                a.getCreatedAt(),
                a.getApprovedAt()
        );
    }
}
