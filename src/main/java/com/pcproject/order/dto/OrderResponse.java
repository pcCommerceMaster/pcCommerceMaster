package com.pcproject.order.dto;

import com.pcproject.order.entity.Order;
import com.pcproject.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomer().getName(),
                order.getProduct().getName(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getAdmin() != null ? order.getAdmin().getName() : null
        );
    }
}
