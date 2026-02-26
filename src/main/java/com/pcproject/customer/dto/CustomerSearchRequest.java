package com.pcproject.customer.dto;

import com.pcproject.customer.entity.CustomerStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchRequest {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final String DEFAULT_DIRECTION = "desc";
    public static final int MIN_PAGE = 1;
    public static final int MIN_SIZE = 1;
    public static final int MAX_SIZE = 100;

    private String keyword = "";
    private CustomerStatus status;

    @Min(value = MIN_PAGE, message = "페이지 번호는 " + MIN_PAGE + " 이상이어야 합니다.")
    private int page = DEFAULT_PAGE;

    @Min(value = MIN_SIZE, message = "페이지 크기는 " + MIN_SIZE + " 이상이어야 합니다.")
    @Max(value = MAX_SIZE, message = "페이지 크기는 " + MAX_SIZE + " 이하이어야 합니다.")
    private int size = DEFAULT_SIZE;

    @Pattern(regexp = "name|email|createdAt", message = "허용되지 않은 정렬 기준입니다.")
    private String sortBy = DEFAULT_SORT_BY;

    @Pattern(regexp = "(?i)asc|desc", message = "정렬 순서는 asc 또는 desc만 허용됩니다.")
    private String direction = DEFAULT_DIRECTION;
}
