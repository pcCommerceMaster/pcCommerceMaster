package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.ProductCategory;
import com.pcproject.pcproduct.entity.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {
    private String name;
    private ProductCategory category;
    private int price;
    private int stock;
    private ProductStatus status;
}
