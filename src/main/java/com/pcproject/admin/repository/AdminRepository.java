package com.pcproject.admin.repository;

import com.pcproject.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {
    public Optional<Admin> findByEmailAndDeletedAtIsNull(String email);
    public Optional<Admin> findByIdAndDeletedAtIsNull(Long id);

    public boolean existsByEmailAndDeletedAtIsNull(String email);
}
