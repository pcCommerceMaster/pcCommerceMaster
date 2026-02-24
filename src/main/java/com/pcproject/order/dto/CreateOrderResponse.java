package com.pcproject.order.dto;


import com.pcproject.order.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class CreateOrderResponse {

    private final Long orderId;
    private final String orderNumber;
    private final OrderStatus status;
    private final Integer quantity;
    private final Long unitPrice;
    private final Long totalAmount;
    private final LocalDateTime createdAt;

    public CreateOrderResponse(Long orderId, String orderNumber, OrderStatus status, Integer quantity, Long unitPrice, Long totalAmount, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.status = status;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }
}
