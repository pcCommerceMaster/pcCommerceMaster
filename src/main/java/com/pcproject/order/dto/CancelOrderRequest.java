package com.pcproject.order.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class CancelOrderRequest {

    @NotBlank
    private String cancelReason;
}
