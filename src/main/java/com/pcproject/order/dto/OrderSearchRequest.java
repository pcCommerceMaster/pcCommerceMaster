package com.pcproject.order.dto;

import com.pcproject.order.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchRequest {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final String DEFAULT_DIRECTION = "desc";

    private String keyword;
    private OrderStatus status;
    private int page = DEFAULT_PAGE;
    private int size = DEFAULT_SIZE;
    private String sortBy = DEFAULT_SORT_BY;
    private String direction = DEFAULT_DIRECTION;
}
