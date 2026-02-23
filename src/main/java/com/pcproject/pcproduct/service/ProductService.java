package com.pcproject.pcproduct.service;

import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.repository.AdminRepository;
import com.pcproject.pcproduct.dto.*;
import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.entity.ProductCategory;
import com.pcproject.pcproduct.entity.ProductStatus;
import com.pcproject.pcproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    // 상품 등록
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
        Product product = new Product(
                request.getProductName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock(),
                request.getStatus(),
                admin
        );

        Product savedProduct = productRepository.save(product);
        return new ProductCreateResponse(savedProduct);
    }

    // 상풀 리스트 조회
    @Transactional(readOnly = true)
    public ProductListResponseWrapper getProducts(ProductSearchRequest request) {

        int page = request.getPage();
        int size = request.getSize();
        String sortBy = request.getSortBy();
        String direction = request.getDirection();
        String keyword = request.getKeyword();
        String category = request.getCategory();
        String status = request.getStatus();

        // 페이지 검증
        if (page < 1) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        // 사이즈 검증
        if (size < 1 || size > 100) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        // 정렬 기준 검증
        if (!sortBy.equals("price") && !sortBy.equals("stock") && !sortBy.equals("createdAt")) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        // 정렬 검증
        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        // Enum 변환 처리(enum 오류 -> 400 대응)
        final ProductCategory categoryEnum;
        final ProductStatus statusEnum;
        try {
            categoryEnum = (category != null)
                    ? ProductCategory.valueOf(category)
                    : null;

            statusEnum = (status != null)
                    ? ProductStatus.valueOf(status)
                    : null;

        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        // 정렬 설정
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page -1, size, sort);

        // 기본 조건 - 삭제 되지 않은 상품만
        Specification<Product> spec =
                (root, query, cb)
                        -> cb.isNull(root.get("deletedAt"));
        //키워드 검색
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and((root, query, cb)
                    -> cb.like(root.get("productName"), "%" + keyword + "%"));
        }
        // 카테고리 필터
        if (category != null) {
            spec = spec.and((root, query, cb)
                    -> cb.equal(root.get("category"), categoryEnum));
        }
        // 상태 필터
        if (status != null) {
            spec = spec.and((root, query, cb)
                    -> cb.equal(root.get("status"), statusEnum));
        }
        Page<Product> productPage =
                productRepository.findAll(spec, pageable);

        return new ProductListResponseWrapper(
                productPage.getContent().stream()
                        .map(ProductListResponse::new).toList(),
                page,
                size,
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }

    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        return new ProductDetailResponse(product);
    }
}
