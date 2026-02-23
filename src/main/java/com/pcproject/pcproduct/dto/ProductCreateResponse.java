package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.entity.ProductCategory;
import com.pcproject.pcproduct.entity.ProductStatus;
import lombok.Getter;

@Getter
public class ProductCreateResponse {
    private Long id;
    private String name;
    private ProductCategory category;
    private int price;
    private int stock;
    private ProductStatus status;

    public ProductCreateResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus();
    }

}
