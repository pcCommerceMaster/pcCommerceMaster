package com.pcproject.order.dto;


import com.pcproject.customer.entity.Customer;
import com.pcproject.pcproduct.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CreateOrderRequest {

    @NotNull
    private Long customerId;

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
