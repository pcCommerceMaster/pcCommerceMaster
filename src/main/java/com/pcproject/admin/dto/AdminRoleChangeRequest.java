package com.pcproject.admin.dto;

import com.pcproject.admin.entity.AdminRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminRoleChangeRequest {

    @NotNull(message = "role은 필수입니다.")
    private AdminRole role;
}
