package com.pcproject.order.dto;


import com.pcproject.order.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class UpdateOrderResponse {

    private final Long orderId;
    private final OrderStatus status;
    private final LocalDateTime updatedAt;

    public UpdateOrderResponse(Long orderId, OrderStatus status, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.status = status;
        this.updatedAt = updatedAt;
    }
}
