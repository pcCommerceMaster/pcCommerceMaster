package com.pcproject.pcproduct.entity;

import com.pcproject.admin.entity.Admin;
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
    }

    // soft delete
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
    public boolean isDeleted() {
        return deletedAt != null;
    }

    // 상품 정보 수정
    public void updateInfo(String productName,
                           ProductCategory category,
                           Long price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    // 재고 증가
    public void increaseStock(int quantity) {
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    // 재고 감소
    public void decreaseStock(int quantity) {
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    // 상태 변경
    public void changeStatus(ProductStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // 재고 기반 상태 자동 동기화
    public void syncStatusByStock() {
        if (this.status == ProductStatus.DISCONTINUED) {
            return;
        }

        if (this.stock <= 0) {
            this.status = ProductStatus.SOLD_OUT;
        } else {
            this.status = ProductStatus.ON_SALE;
        }
    }
}
