package com.pcproject.order.dto;

import com.pcproject.order.entity.OrderStatus;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class OrderDetailResponse {
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String productName;
    private Integer quantity;
    private Long totalAmount;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String adminName;
    private String adminEmail;
    private String adminRole;
}
