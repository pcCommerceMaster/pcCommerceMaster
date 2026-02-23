package com.pcproject.order.service;

import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.order.dto.OrderDetailResponse;
import com.pcproject.order.dto.OrderListResponse;
import com.pcproject.order.dto.OrderResponse;
import com.pcproject.order.dto.PageInfoResponse;
import com.pcproject.order.entity.Order;
import com.pcproject.order.entity.OrderStatus;
import com.pcproject.order.repository.OrderRepository;
import com.pcproject.order.repository.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderListResponse getOrders(String keyword, OrderStatus status, int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy); // asc(오름차순), desc(내림차순) 외 입력 시 예외

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // 키워드 검색 + 상태 필터 조건 조합 (null이면 전체 조회)
        Specification<Order> spec = Specification
                .where(OrderSpecification.searchByKeyword(keyword))
                .and(OrderSpecification.filterByStatus(status));

        Page<Order> orders = orderRepository.findAll(spec, pageable);

        List<OrderResponse> content = orders.getContent().stream()
                .map(OrderResponse::from)
                .toList();

        PageInfoResponse pageInfo = PageInfoResponse.from(orders);

        return new OrderListResponse(content, pageInfo);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return OrderDetailResponse.from(order);
    }
}
