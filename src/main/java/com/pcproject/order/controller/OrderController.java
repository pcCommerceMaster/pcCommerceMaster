package com.pcproject.order.controller;

import com.pcproject.order.dto.OrderDetailResponse;
import com.pcproject.order.dto.OrderListResponse;
import com.pcproject.order.dto.OrderSearchRequest;
import com.pcproject.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

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
