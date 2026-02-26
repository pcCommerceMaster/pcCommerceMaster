package com.pcproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminRejectRequest {

    @NotBlank(message = "거부 사유는 필수입니다.")
    private String reason;
}
