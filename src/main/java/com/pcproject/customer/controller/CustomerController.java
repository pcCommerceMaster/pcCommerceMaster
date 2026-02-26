package com.pcproject.customer.controller;

import com.pcproject.customer.dto.*;
import com.pcproject.customer.service.CustomerService;
import com.pcproject.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<GetCustomerListResponse>> getCustomers(
            @Valid @ModelAttribute CustomerSearchRequest request,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("고객 목록 조회 성공", customerService.getCustomers(request.getKeyword(), pageable)));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<GetCustomerResponse>> getCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.success("고객 상세 조회 성공", customerService.getCustomer(customerId)));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<UpdateCustomerInfoResponse>> updateInfo(
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerInfoRequest request) {
        return ResponseEntity.ok(ApiResponse.success("고객 정보 수정 완료", customerService.updateInfo(customerId, request)));
    }

    @PatchMapping("/{customerId}/status")
    public ResponseEntity<ApiResponse<UpdateCustomerStatusResponse>> updateStatus(
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success("고객 상태 변경 완료", customerService.updateStatus(customerId, request)));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success("고객 삭제 완료", null));
    }
}
