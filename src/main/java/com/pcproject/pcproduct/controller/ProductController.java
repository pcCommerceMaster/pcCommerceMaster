package com.pcproject.pcproduct.controller;

import com.pcproject.global.response.ApiResponse;
import com.pcproject.pcproduct.dto.ProductCreateRequest;
import com.pcproject.pcproduct.dto.ProductCreateResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request,
            @RequestAttribute("adminId") Long adminId
            ) {
        ProductCreateResponse response = productService.createProduct(request, adminId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("상품 등록 완료", response));
    }
}