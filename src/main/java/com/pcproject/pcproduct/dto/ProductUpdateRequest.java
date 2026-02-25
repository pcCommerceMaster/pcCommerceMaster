package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotNull
    private ProductCategory category;

    @NotNull
    @Min(0)
    private Long price;
}
