package com.pcproject.pcproduct.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductStockUpdateRequest {

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private String type; // INCREASE or DECREASE
}
