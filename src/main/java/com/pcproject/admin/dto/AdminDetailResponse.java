package com.pcproject.admin.dto;

import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.entity.AdminRole;
import com.pcproject.admin.entity.AdminStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminDetailResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final AdminRole role;
    private final AdminStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;
    private final LocalDateTime rejectedAt;
    private final String rejectReason;

    private AdminDetailResponse(Long id, String name, String email, String phoneNumber,
                                AdminRole role, AdminStatus status,
                                LocalDateTime createdAt, LocalDateTime approvedAt,
                                LocalDateTime rejectedAt, String rejectReason) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.rejectedAt = rejectedAt;
        this.rejectReason = rejectReason;
    }

    public static AdminDetailResponse from(Admin a) {
        return new AdminDetailResponse(
                a.getId(),
                a.getName(),
                a.getEmail(),
                a.getPhoneNumber(),
                a.getRole(),
                a.getStatus(),
                a.getCreatedAt(),
                a.getApprovedAt(),
                a.getRejectedAt(),
                a.getRejectReason()
        );
    }
}
