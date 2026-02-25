package com.pcproject.pcproduct.controller;

import com.pcproject.admin.controller.SessionConst;
import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.global.response.ApiResponse;
import com.pcproject.pcproduct.dto.*;
import com.pcproject.pcproduct.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

  // 검토필요 [20260225 03:25]===============================================================================
//    @PostMapping
//    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
//            @Valid @RequestBody ProductCreateRequest request,
//            @RequestAttribute("adminId") Long adminId
//            ) {
//        ProductCreateResponse response = productService.createProduct(request, adminId);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.created("상품 등록 완료", response));
//    }
// @PostMapping
// public ApiResponse<ProductCreateResponse> createProduct(
//         @Valid @RequestBody ProductCreateRequest request,
//         HttpServletRequest httpRequest // 세션 직접 꺼내기 위해 추가
// ) {
//     // 인터셉터 구현 전이라 세션에서 직접 꺼내도록 임시 수정했습니다.
//     HttpSession session = httpRequest.getSession(false);
//     if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
//         throw new CustomException(ErrorCode.UNAUTHORIZED);
//     }
//     LoginAdmin loginAdmin = (LoginAdmin) session.getAttribute(SessionConst.LOGIN_ADMIN);

//     ProductCreateResponse response = productService.createProduct(request, loginAdmin.getId());
//     return ApiResponse.created("상품 등록 완료", response);
// }===================================================================================================
    // 상품 등록
//    @PostMapping
//    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
//            @Valid @RequestBody ProductCreateRequest request,
//            @RequestAttribute("adminId") Long adminId
//            ) {
//        ProductCreateResponse response = productService.createProduct(request, adminId);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.created("상품 등록 완료", response));
//    }

// 세션에서 직접 꺼내도록 수정
    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        LoginAdmin loginAdmin = (LoginAdmin) session.getAttribute(SessionConst.LOGIN_ADMIN);

        ProductCreateResponse response = productService.createProduct(request, loginAdmin.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("상품 등록 완료", response));
    }

    // 상품 리스트 조회
    @GetMapping
    public ResponseEntity<ApiResponse<ProductListResponseWrapper>> getProducts(
            @Valid @ModelAttribute ProductSearchRequest request
    ) {
        ProductListResponseWrapper response =
                productService.getProducts(request);
        return ResponseEntity.ok(
                ApiResponse.success("상품 목록 조회 성공", response)
        );
    }

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductDetail(
            @PathVariable Long productId
    ) {
        ProductDetailResponse response =
                productService.getProductDetail(productId);

        return ResponseEntity.ok(
                ApiResponse.success("상품 상세 조회 성공", response)
        );
    }

    // 상품 정보 수정
    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductUpdateResponse>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {

        ProductUpdateResponse response =
                productService.updateProduct(productId, request);

        return ResponseEntity.ok(
                ApiResponse.success("상품 수정 완료", response)
        );
    }

    // 상품 재고 변경
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<ApiResponse<ProductStockUpdateResponse>> updateStock(
            @PathVariable Long productId,
            @Valid @RequestBody ProductStockUpdateRequest request
    ) {

        ProductStockUpdateResponse response =
                productService.updateStock(productId, request);

        return ResponseEntity.ok(
                ApiResponse.success("재고 변경 완료", response)
        );
    }

    // 상품 상태 변경
    @PatchMapping("/{productId}/status")
    public ResponseEntity<ApiResponse<ProductStatusUpdateResponse>> updateStatus(
            @PathVariable Long productId,
            @Valid @RequestBody ProductStatusUpdateRequest request
    ) {

        ProductStatusUpdateResponse response =
                productService.updateStatus(productId, request);

        return ResponseEntity.ok(
                ApiResponse.success("상품 상태 변경 완료", response)
        );
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(
                ApiResponse.success("상품 삭제 완료", null)
        );
    }

    // 상품 복구
    @PatchMapping("/{productId}/restore")
    public ResponseEntity<ApiResponse<ProductStatusUpdateResponse>> restoreProduct(
            @PathVariable Long productId
    ) {
        ProductStatusUpdateResponse response =
                productService.restoreProduct(productId);

        return ResponseEntity.ok(
                ApiResponse.success("상품 복구 완료", response)
        );
    }
}