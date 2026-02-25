package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.ProductCategory;
import com.pcproject.pcproduct.entity.ProductSortType;
import com.pcproject.pcproduct.entity.ProductStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class ProductSearchRequest {
    private String keyword;
    @Min(1)
    private Integer page = 1;

    @Min(1)
    @Max(100)
    private Integer size = 10;
    private ProductSortType sortBy = ProductSortType.CREATED_AT;
    private Sort.Direction direction = Sort.Direction.DESC;
    private ProductCategory category;
    private ProductStatus status;
}