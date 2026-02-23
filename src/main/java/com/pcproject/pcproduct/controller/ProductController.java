package com.pcproject.pcproduct.controller;

import com.pcproject.global.response.ApiResponse;
import com.pcproject.pcproduct.dto.*;
import com.pcproject.pcproduct.service.ProductService;
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

    // 상품 등록
    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request,
            @RequestAttribute("adminId") Long adminId
            ) {
        ProductCreateResponse response = productService.createProduct(request, adminId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("상품 등록 완료", response));
    }

    // 상품 리스트 조회
    @GetMapping
    public ResponseEntity<ApiResponse<ProductListResponseWrapper>> getProducts(
            @ModelAttribute ProductSearchRequest request
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
}