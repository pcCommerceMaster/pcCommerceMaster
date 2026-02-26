package com.pcproject.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCustomerStatusRequest {
    @NotBlank(message = "상태값을 입력해주세요")
    private String status;
}
