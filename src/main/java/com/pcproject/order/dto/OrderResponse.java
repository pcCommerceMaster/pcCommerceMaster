package com.pcproject.order.dto;

import com.pcproject.order.entity.OrderStatus;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String customerName;
    private String productName;
    private Integer quantity;
    private Long totalAmount;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String adminName;
}
