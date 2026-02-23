package com.pcproject.order.controller;

import com.pcproject.order.dto.*;
import com.pcproject.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성(POST)
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        CreateOrderResponse result = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 주문 상태 수정(PATCH)
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<UpdateOrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderRequest request) {

        UpdateOrderResponse result = orderService.updateOrderStatus(orderId, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<OrderListResponse> getOrders(OrderSearchRequest request) {
        OrderListResponse response = orderService.getOrders(
                request.getKeyword(),
                request.getStatus(),
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getDirection()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable Long id) {
        OrderDetailResponse response = orderService.getOrder(id);
        return ResponseEntity.ok(response);
    }
}
