package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductStatusUpdateRequest {
    @NotNull
    private ProductStatus status;
}
