package com.pcproject.order.repository;

import com.pcproject.customer.entity.Customer;
import com.pcproject.order.entity.Order;
import com.pcproject.order.entity.OrderStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

// 검색 조건 동적으로 처리
public class OrderSpecification {
    // 검색 키워드
    public static Specification<Order> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) return criteriaBuilder.conjunction(); // 키워드 비어있으면 전체 조회

            Join<Order, Customer> customerJoin = root.join("customer", JoinType.LEFT); // 고객 정보 삭제 시에도 유지

            Predicate hasKeywordInOrderNumber = criteriaBuilder.like(root.get("orderNumber"), "%" + keyword + "%");
            Predicate hasKeywordInCustomerName = criteriaBuilder.like(customerJoin.get("name"), "%" + keyword + "%");
            return criteriaBuilder.or(hasKeywordInOrderNumber, hasKeywordInCustomerName); // 조건 묶기
        };
    }
    // 주문 상태 필터링
    public static Specification<Order> filterByStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return criteriaBuilder.conjunction(); // 상태 비어있으면 전체 조회

            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
