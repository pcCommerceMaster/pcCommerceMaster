package com.pcproject.order.service;


import com.pcproject.order.dto.CreateOrderRequest;
import com.pcproject.order.dto.CreateOrderResponse;
import com.pcproject.order.dto.UpdateOrderRequest;
import com.pcproject.order.dto.UpdateOrderResponse;
import com.pcproject.order.entity.Order;
import com.pcproject.order.entity.OrderStatus;
import com.pcproject.order.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 2. 20.
 * Time: 오후 7:59
 **/

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // 주문 생성(POST)
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {

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
}
