package com.pcproject.pcproduct.entity;

import com.pcproject.admin.entity.Admin;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 상품명
    @Column(nullable = false, length = 100)
    private String productName;
    // 카테고리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;
    // 가격
    @Column(nullable = false)
    private Long price;
    // 재고
    @Column(nullable = false)
    private Integer stock;
    // 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;
    // 등록 관리자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
    // 생성일
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    // 수정일
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    // soft delete
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Product(String productName,
                   ProductCategory category,
                   Long price,
                   Integer stock,
                   ProductStatus status,
                   Admin admin
    ) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.admin = admin;
        this.createdAt = LocalDateTime.now();
        // 등록시 상태 자동 동기화
        this.syncStatusByStock();
    }


    // 주문 가능 여부 검증
    public void validateOrderable() {
        if (isDeleted()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (this.status != ProductStatus.ON_SALE) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_ON_SALE);
        }
    }

    // 상품 정보 수정
    public void updateInfo(String productName, ProductCategory category, Long price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    // 재고 감소 (주문 시)
    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        if (this.stock < quantity) {
            throw new CustomException(ErrorCode.PRODUCT_STOCK_INSUFFICIENT);
        }

        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();

        // 재고 변경 후 상태 자동 동기화
        syncStatusByStock();
    }

    // 재고 증가 / 복구 (주문 취소 시)
    public void restoreStock(int quantity) {
        if (quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();

        // 단종 상품은 상태 변경하지 않음
        if (this.status != ProductStatus.DISCONTINUED) {
            syncStatusByStock();
        }
    }

    public void increaseStock(int quantity) {
        restoreStock(quantity); // restoreStock과 동일 로직 재사용
    }

    // 상태 변경

    public void changeStatus(ProductStatus next) {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.PRODUCT_DISCONTINUED);
        }

        if (next == ProductStatus.ON_SALE && this.stock <= 0) {
            throw new CustomException(ErrorCode.PRODUCT_STATUS_CONFLICT);
        }

        this.status = next;
        this.updatedAt = LocalDateTime.now();
    }

    // 재고 기반 상태 자동 동기화
    public void syncStatusByStock() {
        if (this.status == ProductStatus.DISCONTINUED) return;

        if (this.stock <= 0) {
            this.status = ProductStatus.SOLD_OUT;
        } else {
            this.status = ProductStatus.ON_SALE;
        }
    }

    // soft delete / 복구
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
        this.updatedAt = LocalDateTime.now();
        syncStatusByStock(); // 복구 시 상태 재보정
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}