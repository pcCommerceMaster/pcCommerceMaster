package com.pcproject.order.service;

import com.pcproject.order.dto.*;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.order.dto.*;
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
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // 주문 생성(POST)
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {

        // 예외 처 - 고객 아이디


        // 주문번호(임시) ORD-생성시간-랜덤
        String orderNumber =
                "ORD-" + System.currentTimeMillis()
                + "-" + UUID.randomUUID().toString().substring(0, 4);

        // 상품 가격(임시)
        Long unitPrice = 10000L;

        // 관리자 Id 임시로 null값 넣음
        Order order = new Order(
                orderNumber,
                request.getCustomerId(),
                request.getProductId(),
                null,
                request.getQuantity(),
                unitPrice,
                OrderStatus.PREPARING
        );

        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus(),
                savedOrder.getQuantity(),
                savedOrder.getUnitPrice(),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt()
        );
    }

    @Transactional
    public UpdateOrderResponse updateOrderStatus(Long orderId, UpdateOrderRequest request) {

        // 1) 주문 조회(추후 공통 에러 코드로 변경 예정)
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("ORDER_NOT_FOUND"));

        // 2) 최종 상태면 변경 불가(추후 공통 에러 코드로 변경 예정)
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("INVALID_ORDER_STATUS");
        }

        OrderStatus target = request.getStatus();
        OrderStatus current = order.getStatus();

        // 3) 허용된 전이만 통과
        boolean isValid =
                (current == OrderStatus.PREPARING && target == OrderStatus.SHIPPING) ||
                        (current == OrderStatus.SHIPPING && target == OrderStatus.DELIVERED);

        if (!isValid) {
            throw new IllegalStateException("INVALID_ORDER_STATUS");
        }

        // 4) 상태 변경 + updatedAt 갱신 (엔티티 메서드로 하는 게 좋음)
        order.updateStatus(target);

        // 5) 저장
        orderRepository.save(order);

        return new UpdateOrderResponse(order.getId(), order.getStatus(), order.getUpdatedAt());
    }


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
