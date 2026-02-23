package com.pcproject.admin.repository;

import com.pcproject.admin.entity.Admin;
import com.pcproject.admin.entity.AdminRole;
import com.pcproject.admin.entity.AdminStatus;
import org.springframework.data.jpa.domain.Specification;

public class AdminSpecifications {
    public static Specification<Admin> notDeleted() {
        return(root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<Admin> keywordLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return cb.conjunction();
            String like = "%" + keyword.trim() + "%";
            return cb.or(
                    cb.like(root.get("name"), like),
                    cb.like(root.get("email"), like)
            );
        };
    }

    public static Specification<Admin> roleEq(AdminRole role) {
        return (root, query, cb) -> role == null ? cb.conjunction() : cb.equal(root.get("role"), role);
    }

    public static Specification<Admin> statusEq(AdminStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }
}
