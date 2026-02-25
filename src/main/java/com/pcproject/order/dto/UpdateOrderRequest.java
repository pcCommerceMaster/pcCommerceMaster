package com.pcproject.order.dto;


import com.pcproject.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class UpdateOrderRequest {

    @NotNull
    private OrderStatus status;
}
