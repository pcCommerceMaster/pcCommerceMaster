package com.pcproject.pcproduct.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductListResponseWrapper {
    private final List<ProductListResponse> content;
    private final PageInfo pageInfo;

    public ProductListResponseWrapper(List<ProductListResponse> content,
                                      int currentPage,
                                      int pageSize,
                                      long totalCount,
                                      int totalPages) {
        this.content = content;
        this.pageInfo = new PageInfo(currentPage, pageSize, totalCount, totalPages);
    }

    @Getter
    public static class PageInfo {
        private final int currentPage;
        private final int pageSize;
        private final long totalCount;
        private final int totalPages;

        public PageInfo(int currentPage, int pageSize,
                        long totalCount, int totalPages) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalCount = totalCount;
            this.totalPages = totalPages;
        }
    }
}