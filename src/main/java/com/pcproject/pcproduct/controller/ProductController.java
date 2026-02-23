package com.pcproject.pcproduct.controller;

import com.pcproject.global.response.ApiResponse;
import com.pcproject.pcproduct.dto.ProductCreateRequest;
import com.pcproject.pcproduct.dto.ProductCreateResponse;
import com.pcproject.pcproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest request) {
        ProductCreateResponse response = productService.createProduct(request);

        return ResponseEntity.status(201)
                .body(ApiResponse.created("상품 등록 성공", response));
    }
}