package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductDetailResponse {
    private final Long id;
    private final String productName;
    private final String category;
    private final Long price;
    private final Integer stock;
    private final String status;
    private final LocalDateTime createdAt;
    private final String adminName;
    private final String adminEmail;

    public ProductDetailResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.category = product.getCategory().name();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus().name();
        this.createdAt = product.getCreatedAt();
        this.adminName = product.getAdmin().getName();
        this.adminEmail = product.getAdmin().getEmail();
    }
}
