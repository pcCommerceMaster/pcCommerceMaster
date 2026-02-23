package com.pcproject.pcproduct.repository;

import com.pcproject.pcproduct.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
