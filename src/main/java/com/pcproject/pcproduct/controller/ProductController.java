package com.pcproject.pcproduct.controller;

import com.pcproject.admin.controller.SessionConst;
import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.global.response.ApiResponse;
import com.pcproject.pcproduct.dto.ProductCreateRequest;
import com.pcproject.pcproduct.dto.ProductCreateResponse;
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
@PostMapping
public ApiResponse<ProductCreateResponse> createProduct(
        @Valid @RequestBody ProductCreateRequest request,
        HttpServletRequest httpRequest // 세션 직접 꺼내기 위해 추가
) {
    // 인터셉터 구현 전이라 세션에서 직접 꺼내도록 임시 수정했습니다.
    HttpSession session = httpRequest.getSession(false);
    if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
        throw new CustomException(ErrorCode.UNAUTHORIZED);
    }
    LoginAdmin loginAdmin = (LoginAdmin) session.getAttribute(SessionConst.LOGIN_ADMIN);

    ProductCreateResponse response = productService.createProduct(request, loginAdmin.getId());
    return ApiResponse.created("상품 등록 완료", response);
}
}