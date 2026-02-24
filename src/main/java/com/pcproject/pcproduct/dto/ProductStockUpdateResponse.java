package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.entity.ProductStatus;
import lombok.Getter;

@Getter
public class ProductStockUpdateResponse {
    private final Long id;
    private final Integer stock;
    private final ProductStatus status;

    public ProductStockUpdateResponse(Product product) {
        this.id = product.getId();
        this.stock = product.getStock();
        this.status = product.getStatus();
    }
}