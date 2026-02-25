package com.pcproject.pcproduct.service;

import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.repository.AdminRepository;
import com.pcproject.pcproduct.dto.*;
import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.entity.ProductCategory;
import com.pcproject.pcproduct.entity.ProductStatus;
import com.pcproject.pcproduct.entity.StockChangeType;
import com.pcproject.pcproduct.repository.ProductRepository;
import jakarta.persistence.criteria.JoinType;
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

    // 상품 리스트 조회
    @Transactional(readOnly = true)
    public ProductListResponseWrapper getProducts(ProductSearchRequest request) {

        int page = request.getPage();
        int size = request.getSize();
        String sortBy = request.getSortBy();
        String direction = request.getDirection();
        String keyword = request.getKeyword();

        // 정렬 기준 검증
        if (!sortBy.equals("price") && !sortBy.equals("stock") && !sortBy.equals("createdAt")) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        // 정렬 검증
        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        // 정렬 설정
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page -1, size, sort);

        // 기본 조건 - 삭제 되지 않은 상품만
        Specification<Product> spec = (root, query, cb) -> {

            if (!Long.class.equals(query.getResultType())) {
                root.fetch("admin", JoinType.INNER);
                query.distinct(true);
            }

            return cb.isNull(root.get("deletedAt"));
        };
        //키워드 검색
        if (keyword != null && !keyword.isBlank()) {

            String safeKeyword = keyword
                    .replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");

            spec = spec.and((root, query, cb)
                    -> cb.like(root.get("productName"),
                    "%" + safeKeyword + "%",
                    '\\'));
        }
        // 카테고리 필터
        if (request.getCategory() != null) {
            spec = spec.and((root, query, cb)
                    -> cb.equal(root.get("category"), request.getCategory()));
        }
        // 상태 필터
        if (request.getStatus() != null) {
            spec = spec.and((root, query, cb)
                    -> cb.equal(root.get("status"), request.getStatus()));
        }
        Page<Product> productPage =
                productRepository.findAll(spec, pageable);

        return new ProductListResponseWrapper(
                productPage.getContent().stream()
                        .map(ProductListResponse::new).toList(),
                productPage.getNumber() + 1,
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }

    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = productRepository.findWithAdminByIdAndDeletedAtIsNull(productId)
                .orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        return new ProductDetailResponse(product);
    }

    // 상품 정보 수정
    @Transactional
    public ProductUpdateResponse updateProduct(Long productId,
                                               ProductUpdateRequest request) {

        Product product = productRepository
                .findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.updateInfo(
                request.getProductName(),
                request.getCategory(),
                request.getPrice()
        );

        return new ProductUpdateResponse(product);
    }

    // 상품 재고 변경
    @Transactional
    public ProductStockUpdateResponse updateStock(Long productId,
                                                  ProductStockUpdateRequest request) {

        Product product = productRepository
                .findWithLockByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() == ProductStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.PRODUCT_DISCONTINUED);
        }

        if (request.getType() == StockChangeType.INCREASE) {
            product.increaseStock(request.getQuantity());
        } else {
            product.decreaseStock(request.getQuantity());
        }

        product.syncStatusByStock();
        return new ProductStockUpdateResponse(product);
    }

    // 상품 상태 변경
    @Transactional
    public ProductStatusUpdateResponse updateStatus(Long productId,
                                                    ProductStatusUpdateRequest request) {

        Product product = productRepository
                .findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductStatus current = product.getStatus();
        ProductStatus next = request.getStatus();

        // DISCONTINUED 상태 변경 불가
        if (current == ProductStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.PRODUCT_DISCONTINUED);
        }
        // stock=0 일때 ON_SALE 불가
        if (next == ProductStatus.ON_SALE && product.getStock() <= 0) {
            throw new CustomException(ErrorCode.PRODUCT_STATUS_CONFLICT);
        }
        // 상태 전이 규칙 검증
        boolean isValid = switch (current) {
            case ON_SALE -> next == ProductStatus.SOLD_OUT || next == ProductStatus.DISCONTINUED;
            case SOLD_OUT -> next == ProductStatus.ON_SALE || next == ProductStatus.DISCONTINUED;
            default -> false;
        };
        if (!isValid) {
            throw new CustomException(ErrorCode.PRODUCT_STATUS_CONFLICT);
        }
        product.changeStatus(next);
        return new ProductStatusUpdateResponse(product);
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.
                findByIdAndDeletedAtIsNull(productId).orElseThrow(
                        () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
                );
        product.softDelete();
    }

    // 상품 복구
    @Transactional
    public ProductStatusUpdateResponse restoreProduct(Long productId) {
        Product product = productRepository
                .findById(productId).orElseThrow(
                        () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // deleted_at IS NULL 이면 복구 불가
        if (!product.isDeleted()){
            throw new CustomException(ErrorCode.PRODUCT_NOT_DELETED);
        }
        product.restore();
        product.syncStatusByStock();

        return new ProductStatusUpdateResponse(product);
    }
}
