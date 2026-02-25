package com.pcproject.pcproduct.repository;

import com.pcproject.pcproduct.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndDeletedAtIsNull(Long id);
}
