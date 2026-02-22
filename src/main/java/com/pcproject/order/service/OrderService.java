package com.pcproject.order.service;


import com.pcproject.order.dto.CreateOrderRequest;
import com.pcproject.order.dto.CreateOrderResponse;
import com.pcproject.order.entity.Order;
import com.pcproject.order.entity.OrderStatus;
import com.pcproject.order.repository.OrderRepository;
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
}
