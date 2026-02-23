package com.pcproject.pcproduct.service;

import com.pcproject.pcproduct.dto.ProductCreateRequest;
import com.pcproject.pcproduct.dto.ProductCreateResponse;
import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock(),
                request.getStatus()
        );

        Product savedProduct = productRepository.save(product);
        return new ProductCreateResponse(savedProduct);
    }
}
