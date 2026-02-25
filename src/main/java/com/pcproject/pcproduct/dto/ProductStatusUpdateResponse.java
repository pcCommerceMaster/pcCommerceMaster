package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.entity.ProductStatus;
import lombok.Getter;

@Getter
public class ProductStatusUpdateResponse {
    private final Long id;
    private final ProductStatus status;

    public ProductStatusUpdateResponse(Product product) {
        this.id = product.getId();
        this.status = product.getStatus();
    }
}