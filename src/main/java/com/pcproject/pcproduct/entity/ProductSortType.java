package com.pcproject.pcproduct.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ProductSortType {
    PRICE("price"),
    STOCK("stock"),
    CREATED_AT("createdAt");

    private final String field;
}