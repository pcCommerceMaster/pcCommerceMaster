package com.pcproject.admin.dto;

import com.pcproject.admin.entity.Admin;
import lombok.Getter;

@Getter
public class MeResponse {

    private final String name;
    private final String email;
    private final String phoneNumber;

    public MeResponse(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static MeResponse from(Admin a) {
        return new MeResponse(
                a.getName(),
                a.getEmail(),
                a.getPhoneNumber()
        );
    }
}
