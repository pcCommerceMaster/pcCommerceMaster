package com.pcproject.order.dto;

import com.pcproject.order.entity.Order;
import com.pcproject.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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

    public static OrderDetailResponse from(Order order) {
        return new OrderDetailResponse(
                order.getOrderNumber(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getProduct().getName(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getAdmin() != null ? order.getAdmin().getName() : null,
                order.getAdmin() != null ? order.getAdmin().getEmail() : null,
                order.getAdmin() != null ? order.getAdmin().getRole() : null
        );
    }
}
