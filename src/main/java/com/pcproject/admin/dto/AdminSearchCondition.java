package com.pcproject.admin.dto;

import com.pcproject.admin.entity.AdminRole;
import com.pcproject.admin.entity.AdminStatus;
import lombok.Getter;

@Getter
public class AdminSearchCondition {

    private String keyword;
    private AdminRole role;
    private AdminStatus status;

    private int page = 1;
    private int size = 10;
    private String sortBy = "createdAt";
    private String direction = "desc";
}
