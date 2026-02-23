package com.pcproject.admin.dto;

import com.pcproject.admin.entity.AdminRole;
import lombok.Getter;

@Getter
public class LoginAdmin {
    private final Long id;
    private final String email;
    private final AdminRole role;

    public LoginAdmin(Long id, String email, AdminRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
