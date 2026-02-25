package com.pcproject.customer.repository;

import com.pcproject.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

        // 상세조회용 - 미삭제 고객만 검색 가능
        Optional<Customer> findByIdAndDeletedAtIsNull(Long id);

        // 키워드 검색(이름 또는 이메일 포함)
//        Page<Customer> findByNameContainingAndDeletedAtIsNullOrEmailContainingAndDeletedAtIsNull(
//                String name, String email, Pageable pageable);
        @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL AND (c.name LIKE %:keyword% OR c.email LIKE %:keyword%)")
        Page<Customer> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

        // 이메일 중복 체크
        boolean existsByEmailAndDeletedAtIsNull(String email);
    }

