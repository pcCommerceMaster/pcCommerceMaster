package com.pcproject.admin.controller;

import com.pcproject.admin.dto.*;
import com.pcproject.admin.entity.AdminRole;
import com.pcproject.admin.entity.AdminStatus;
import com.pcproject.admin.service.AdminManagementService;
import com.pcproject.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminManagementController {

    private final AdminManagementService adminManagementService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AdminSummaryResponse>>> list(
            @ModelAttribute AdminSearchCondition condition
    ) {
        Page<AdminSummaryResponse> result =
                adminManagementService.list(condition);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("관리자 목록 조회 성공", PageResponse.from(result)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminDetailResponse>> detail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("관리자 상세 조회 성공", adminManagementService.detail(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @Valid @RequestBody AdminUpdateRequest req) {
        adminManagementService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("관리자 정보 수정 성공", null));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<Void>> changeRole(@PathVariable Long id, @Valid @RequestBody AdminRoleChangeRequest req) {
        adminManagementService.changeRole(id, req);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("관리자 역할 변경 성공", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(@PathVariable Long id, @Valid @RequestBody AdminStatusChangeRequest req) {
        adminManagementService.changeStatus(id, req);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("관리자 상태 변경 성공", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminManagementService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("관리자 삭제 성공", null));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(@PathVariable Long id) {
        adminManagementService.approve(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("관리자 승인 성공", null));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(@PathVariable Long id, @Valid @RequestBody AdminRejectRequest req) {
        adminManagementService.reject(id, req);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("관리자 거부 성공", null));
    }
}
