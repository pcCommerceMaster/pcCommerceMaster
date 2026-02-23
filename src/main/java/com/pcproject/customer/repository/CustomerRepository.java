package com.pcproject.customer.repository;

import com.pcproject.customer.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

        // 상세조회용 - 미삭제 고객만 검색 가능
        Optional<CustomerEntity> findByIdAndDeletedAtIsNull(Long id);

        // 키워드 검색(이름 또는 이메일 포함)
        Page<CustomerEntity> findByNameContainingAndDeletedAtIsNullOrEmailContainingAndDeletedAtIsNull(
                String name, String email, Pageable pageable);

        // 이메일 중복 체크
        boolean existsByEmailAndDeletedAtIsNull(String email);
    }

