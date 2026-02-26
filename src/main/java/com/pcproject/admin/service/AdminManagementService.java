package com.pcproject.admin.service;

import com.pcproject.admin.dto.*;
import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.entity.AdminRole;
import com.pcproject.admin.entity.AdminStatus;
import com.pcproject.admin.repository.AdminRepository;
import com.pcproject.admin.repository.AdminSpecifications;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminManagementService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public Page<AdminSummaryResponse> list(AdminSearchCondition condition) {

        String keyword = condition.getKeyword();
        AdminRole role = condition.getRole();
        AdminStatus status = condition.getStatus();

        int page = condition.getPage();
        int size = condition.getSize();
        String sortBy = condition.getSortBy();
        String direction = condition.getDirection();

        // page는 1부터 받고 PageRequest는 0부터여서 -1
        int pageIndex = Math.max(page - 1, 0);

        // direction 값이 이상하게 들어오면 기본값 DESC로 안전하게
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction); // "asc" / "desc"
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.DESC;
        }

        // sortBy가 비었으면 기본값
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdAt";
        }

        // size가 0 이하로 들어오면 기본값
        if (size <= 0) {
            size = 10;
        }

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(pageIndex, size, sort);

        Specification<Admin> spec = Specification
                .where(AdminSpecifications.notDeleted())
                .and(AdminSpecifications.keywordLike(keyword))
                .and(AdminSpecifications.roleEq(role))
                .and(AdminSpecifications.statusEq(status));

        return adminRepository.findAll(spec, pageable)
                .map(AdminSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public AdminDetailResponse detail(Long id) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
        return AdminDetailResponse.from(admin);
    }

    @Transactional
    public void update(Long id, AdminUpdateRequest req) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (!req.getEmail().equals(admin.getEmail())
                && adminRepository.existsByEmailAndDeletedAtIsNull(req.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        admin.updateProfile(req.getName(), req.getEmail(), req.getPhoneNumber());
    }

    @Transactional
    public void changeRole(Long id, AdminRoleChangeRequest req) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
        admin.changeRole(req.getRole());
    }

    @Transactional
    public void changeStatus(Long id, AdminStatusChangeRequest req) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
        admin.changeStatus(req.getStatus());
    }

    @Transactional
    public void approve(Long id) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        try {
            admin.approve();
        } catch (IllegalStateException e) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }

    @Transactional
    public void reject(Long id, AdminRejectRequest req) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        try {
            admin.reject(req.getReason());
        } catch (IllegalStateException e) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }

    @Transactional
    public void delete(Long id) {
        Admin admin = adminRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
        admin.softDelete();
    }
}
