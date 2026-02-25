package com.pcproject.order.repository;

import com.pcproject.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.product JOIN FETCH o.admin WHERE o.id = :id")
    Optional<Order> findWithDetailsById(@Param("id") Long id);
}
