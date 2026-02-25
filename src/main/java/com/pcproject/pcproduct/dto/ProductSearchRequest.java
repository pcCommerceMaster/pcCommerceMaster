package com.pcproject.pcproduct.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {
    private String keyword;
    private Integer page = 1;
    private Integer size = 10;
    private String sortBy = "createdAt";
    private String direction = "desc";
    private String category;
    private String status;
}
