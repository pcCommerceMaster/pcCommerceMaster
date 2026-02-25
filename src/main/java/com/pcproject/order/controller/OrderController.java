package com.pcproject.order.controller;

import com.pcproject.admin.controller.SessionConst;
import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.order.dto.*;
import com.pcproject.order.service.OrderService;
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

    // 주문 생성 (세션 기반 관리자 인증)
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            HttpSession session) {

        LoginAdmin loginAdmin =
                (LoginAdmin) session.getAttribute(SessionConst.LOGIN_ADMIN);

        CreateOrderResponse result = orderService.createOrder(request, loginAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 주문 상태 수정(PATCH)
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<UpdateOrderResponse> updateOrderStatus(
            @PathVariable @Positive Long orderId,
            @Valid @RequestBody UpdateOrderRequest request) {

        UpdateOrderResponse result = orderService.updateOrderStatus(orderId, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 주문 취소(PATCH)
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(
            @PathVariable @Positive Long orderId,
            @Valid @RequestBody CancelOrderRequest request) {
        CancelOrderResponse result = orderService.cancelOrder(orderId, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping
    public ResponseEntity<OrderListResponse> getOrders(@Valid OrderSearchRequest request) {
        OrderListResponse response = orderService.getOrders(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable Long orderId) {
        OrderDetailResponse response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }
}
