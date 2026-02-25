package com.pcproject.pcproduct.repository;

import com.pcproject.pcproduct.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    // 기본 단건 조회(soft delete 제외)
    Optional<Product> findByIdAndDeletedAtIsNull(Long id);
    // 재고 변견용 비관적 락 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findWithLockByIdAndDeletedAtIsNull(Long id);

    // 상세 조회용
    @Query("""
            SELECT p
            FROM Product p
            JOIN FETCH p.admin
            WHERE p.id = :id
            AND p.deletedAt IS NULL
            """)
    Optional<Product> findWithAdminByIdAndDeletedAtIsNull(@Param("id") Long id);
}
