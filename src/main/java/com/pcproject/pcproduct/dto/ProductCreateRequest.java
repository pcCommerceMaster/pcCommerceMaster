package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.ProductCategory;
import com.pcproject.pcproduct.entity.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotNull
    private ProductCategory category;

    @NotNull
    @Min(0)
    private Long price;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    private ProductStatus status;
}
