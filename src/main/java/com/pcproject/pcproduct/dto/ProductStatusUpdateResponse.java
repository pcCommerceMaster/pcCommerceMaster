package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.Product;
import lombok.Getter;

@Getter
public class ProductStatusUpdateResponse {
    private final Long id;
    private final String status;

    public ProductStatusUpdateResponse(Product product) {
        this.id = product.getId();
        this.status = product.getStatus().name();
    }
}
