package com.pcproject.order.controller;


import com.pcproject.order.dto.CreateOrderRequest;
import com.pcproject.order.dto.CreateOrderResponse;
import com.pcproject.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
