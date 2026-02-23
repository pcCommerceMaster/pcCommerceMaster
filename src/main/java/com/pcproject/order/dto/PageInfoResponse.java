package com.pcproject.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PageInfoResponse {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalCount;

    public static PageInfoResponse from(Page<?> page) {
        return new PageInfoResponse(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
