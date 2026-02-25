package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.entity.ProductCategory;
import com.pcproject.pcproduct.entity.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductUpdateResponse {
    private final Long id;
    private final String productName;
    private final ProductCategory category;
    private final Long price;
    private final Integer stock;
    private final ProductStatus status;
    private final LocalDateTime updatedAt;

    public ProductUpdateResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus();
        this.updatedAt = product.getUpdatedAt();
    }
}
