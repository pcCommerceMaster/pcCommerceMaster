package com.pcproject.order.dto;


import com.pcproject.order.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class CancelOrderResponse {

    private final Long orderId;
    private final OrderStatus status;
    private final String cancelReason;
    private final LocalDateTime updatedAt;

    public CancelOrderResponse(Long orderId, OrderStatus status, String cancelReason, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.status = status;
        this.cancelReason = cancelReason;
        this.updatedAt = updatedAt;
    }
}
