package com.pcproject.admin.controller;

import com.pcproject.admin.dto.*;
import com.pcproject.admin.entity.AdminRole;
import com.pcproject.admin.entity.AdminStatus;
import com.pcproject.admin.service.AdminManagementService;
import com.pcproject.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminManagementController {

    private final AdminManagementService adminManagementService;

    @GetMapping
    public ApiResponse<PageResponse<AdminSummaryResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) AdminRole role,
            @RequestParam(required = false) AdminStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Page<AdminSummaryResponse> result =
                adminManagementService.list(keyword, role, status, page, size, sortBy, direction);
        return ApiResponse.success("관리자 목록 조회 성공", PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminDetailResponse> detail(@PathVariable Long id) {
        return ApiResponse.success("관리자 상세 조회 성공", adminManagementService.detail(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody AdminUpdateRequest req) {
        adminManagementService.update(id, req);
        return ApiResponse.success("관리자 정보 수정 성공", null);
    }

    @PatchMapping("/{id}/role")
    public ApiResponse<Void> changeRole(@PathVariable Long id, @Valid @RequestBody AdminRoleChangeRequest req) {
        adminManagementService.changeRole(id, req);
        return ApiResponse.success("관리자 역할 변경 성공", null);
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> changeStatus(@PathVariable Long id, @Valid @RequestBody AdminStatusChangeRequest req) {
        adminManagementService.changeStatus(id, req);
        return ApiResponse.success("관리자 상태 변경 성공", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        adminManagementService.delete(id);
        return ApiResponse.success("관리자 삭제 성공", null);
    }

    @PatchMapping("/{id}/approve")
    public ApiResponse<Void> approve(@PathVariable Long id) {
        adminManagementService.approve(id);
        return ApiResponse.success("관리자 승인 성공", null);
    }

    @PatchMapping("/{id}/reject")
    public ApiResponse<Void> reject(@PathVariable Long id, @Valid @RequestBody AdminRejectRequest req) {
        adminManagementService.reject(id, req);
        return ApiResponse.success("관리자 거부 성공", null);
    }
}
