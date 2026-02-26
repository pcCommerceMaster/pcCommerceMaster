package com.pcproject.admin.dto;

import com.pcproject.admin.entity.AdminStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminStatusChangeRequest {

    @NotNull(message = "status는 필수입니다.")
    private AdminStatus status;
}
