package com.pcproject.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "admins", uniqueConstraints = @UniqueConstraint(name = "uk_admin_email", columnNames = "email"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AdminRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "reject_reason", length = 255)
    private String rejectReason;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Admin(String name, String email, String encodedPassword, String phoneNumber, AdminRole role) {
        this.name = name;
        this.email = email;
        this.password = encodedPassword;
        this.phoneNumber = phoneNumber;
        this.role = role;

        this.status = AdminStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void approve() {
        if (isDeleted()) throw new IllegalStateException("삭제된 계정입니다.");
        if (status != AdminStatus.PENDING) throw new IllegalStateException("승인대기 상태만 승인 가능합니다.");
        status = AdminStatus.ACTIVE;
        approvedAt = LocalDateTime.now();
        rejectedAt = null;
        rejectReason = null;
    }

    public void reject(String reason) {
        if (isDeleted()) throw new IllegalStateException("삭제된 계정입니다.");
        if (status != AdminStatus.PENDING) throw new IllegalStateException("승인대기 상태만 거부 가능합니다.");
        status = AdminStatus.REJECTED;
        rejectedAt = LocalDateTime.now();
        rejectReason = reason;
    }

    public void updateProfile(String name, String email, String phoneNumber) {
        if (isDeleted()) throw new IllegalStateException("삭제된 계정입니다.");
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void changeRole(AdminRole role) {
        if (isDeleted()) throw new IllegalStateException("삭제된 계정입니다.");
        this.role = role;
    }

    public void changeStatus(AdminStatus status) {
        if (isDeleted()) throw new IllegalStateException("삭제된 계정입니다.");
        this.status = status;
    }

    public void changePassword(String encodedPassword) {
        if (isDeleted()) throw new IllegalStateException("삭제된 계정입니다.");
        this.password = encodedPassword;
    }
}


