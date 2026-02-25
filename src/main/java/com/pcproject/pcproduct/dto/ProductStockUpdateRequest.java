package com.pcproject.pcproduct.dto;

import com.pcproject.pcproduct.entity.StockChangeType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductStockUpdateRequest {

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private StockChangeType type;
}
