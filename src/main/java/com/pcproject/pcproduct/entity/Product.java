package com.pcproject.pcproduct.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private int price;
    private int stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Product(String name,
                   ProductCategory category,
                   int price,
                   int stock,
                   ProductStatus status
                   ) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }
}
