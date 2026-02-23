package com.pcproject.pcproduct.service;

import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.repository.AdminRepository;
import com.pcproject.pcproduct.dto.ProductCreateRequest;
import com.pcproject.pcproduct.dto.ProductCreateResponse;
import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
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
}
