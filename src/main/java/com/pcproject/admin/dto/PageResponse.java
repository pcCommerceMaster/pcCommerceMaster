package com.pcproject.admin.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElemnets;
    private final int totalPages;

    public PageResponse(List<T> content, int page, int size, long totalElemnets, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElemnets = totalElemnets;
        this.totalPages = totalPages;
    }

    public static <T> PageResponse<T> from(Page<T> pageData) {
        return new PageResponse<T>(
                pageData.getContent(),
                pageData.getNumber() + 1,
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages()
        );
    }
}
