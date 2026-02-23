package com.pcproject.order.controller;


import com.pcproject.order.dto.CreateOrderRequest;
import com.pcproject.order.dto.CreateOrderResponse;
import com.pcproject.order.dto.UpdateOrderRequest;
import com.pcproject.order.dto.UpdateOrderResponse;
import com.pcproject.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 2. 20.
 * Time: 오후 7:57
 **/

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


}
