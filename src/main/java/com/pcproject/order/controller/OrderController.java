package com.pcproject.order.controller;

import com.pcproject.admin.entity.Admin;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.global.response.ApiResponse;
import com.pcproject.order.dto.*;
import com.pcproject.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RequestMapping("/api/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Interceptor에서 주입된 Admin 추출 (방어적 null 체크)
    private Admin extractAdmin(HttpServletRequest request) {
        Admin admin = (Admin) request.getAttribute("admin");
        if (admin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return admin;
    }

    // 주문 생성 API
    // 인증/인가 검증은 Interceptor에서 수행됨
    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            HttpServletRequest httpRequest) {
        Admin admin = extractAdmin(httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("주문 생성 완료", orderService.createOrder(request, admin)));
    }

    // 주문 상태 API
    // 인증/인가 검증은 Interceptor에서 수행됨
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<UpdateOrderResponse>> updateOrderStatus(
            @PathVariable @Positive Long orderId,
            @Valid @RequestBody UpdateOrderRequest request,
            HttpServletRequest httpRequest) {
        Admin admin = extractAdmin(httpRequest);
        return ResponseEntity.ok(ApiResponse.success("주문 상태 변경 완료", orderService.updateOrderStatus(orderId, request, admin)));
    }

    // 주문 취소 API
    // 인증/인가 검증은 Interceptor에서 수행됨
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<CancelOrderResponse>> cancelOrder(
            @PathVariable @Positive Long orderId,
            @Valid @RequestBody CancelOrderRequest request,
            HttpServletRequest httpRequest) {
        Admin admin = extractAdmin(httpRequest);
        return ResponseEntity.ok(ApiResponse.success("주문 취소 완료", orderService.cancelOrder(orderId, request, admin)));
    }



    @GetMapping
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrders(@ModelAttribute @Valid OrderSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success("주문 목록 조회 성공", orderService.getOrders(request)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success("주문 상세 조회 성공", orderService.getOrder(orderId)));
    }
}
