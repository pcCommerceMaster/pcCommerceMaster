package com.pcproject.order.entity;

import com.pcproject.admin.entity.Admin;
import com.pcproject.customer.entity.Customer;
import com.pcproject.pcproduct.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    private LocalDateTime deletedAt;

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

    // 주문 상태 변경 메서드
    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // 주문 취소 메서드
    public void cancel(String cancelReason) {
        this.status = OrderStatus.CANCELLED;
        this.cancelReason = cancelReason;
        this.updatedAt = LocalDateTime.now();
    }
}
