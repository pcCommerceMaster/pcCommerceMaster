package com.pcproject.order.controller;

import com.pcproject.admin.controller.SessionConst;
import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.admin.entity.Admin;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.order.dto.*;
import com.pcproject.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            HttpServletRequest httpRequest) {

        Admin admin = extractAdmin(httpRequest);

        CreateOrderResponse result = orderService.createOrder(request, admin);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 주문 상태 API
    // 인증/인가 검증은 Interceptor에서 수행됨
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<UpdateOrderResponse> updateOrderStatus(
            @PathVariable @Positive Long orderId,
            @Valid @RequestBody UpdateOrderRequest request,
            HttpServletRequest httpRequest) {

        Admin admin = extractAdmin(httpRequest);

        UpdateOrderResponse result = orderService.updateOrderStatus(orderId, request, admin);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 주문 취소 API
    // 인증/인가 검증은 Interceptor에서 수행됨
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(
            @PathVariable @Positive Long orderId,
            @Valid @RequestBody CancelOrderRequest request,
            HttpServletRequest httpRequest) {

        Admin admin = extractAdmin(httpRequest);

        CancelOrderResponse result = orderService.cancelOrder(orderId, request, admin);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping
    public ResponseEntity<OrderListResponse> getOrders(@Valid OrderSearchRequest request) {
        OrderListResponse response = orderService.getOrders(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable Long orderId) {
        OrderDetailResponse response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }
}
