package com.pcproject.order.entity;

import com.pcproject.admin.entity.Admin;
import com.pcproject.customer.entity.Customer;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.pcproduct.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long unitPrice;

    @Column(nullable = false)
    private Long totalAmount;

    private String cancelReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Order(String orderNumber, Customer customer, Product product, Admin admin, Integer quantity, Long unitPrice, OrderStatus status) {
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.product = product;
        this.admin = admin;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = unitPrice * quantity;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    // 상태 전이 정책을 캡슐화한 메서드 (허용된 전이만 가능)
    public void changeStatus(OrderStatus target) {

        if (target == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        if (this.status == OrderStatus.CANCELLED
                || this.status == OrderStatus.DELIVERED) {
            throw new CustomException(ErrorCode.ORDER_INVALID_STATUS);
        }

        boolean valid =
                (this.status == OrderStatus.PREPARING && target == OrderStatus.SHIPPING) ||
                        (this.status == OrderStatus.SHIPPING && target == OrderStatus.DELIVERED);

        if (!valid) {
            throw new CustomException(ErrorCode.ORDER_INVALID_STATUS);
        }

        this.status = target;
        this.updatedAt = LocalDateTime.now();
    }

    // 주문 취소 정책 수행 (사유 검증 + PREPARING 상태만 허용)
    public void cancel(String cancelReason) {

        // 취소 사유는 필수 (도메인 무결성 보장)
        if (cancelReason == null || cancelReason.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        // 취소는 PREPARING 상태에서만 가능
        if (this.status != OrderStatus.PREPARING) {
            throw new CustomException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
        }

        this.status = OrderStatus.CANCELLED;
        this.cancelReason = cancelReason;
        this.updatedAt = LocalDateTime.now();
    }

}
