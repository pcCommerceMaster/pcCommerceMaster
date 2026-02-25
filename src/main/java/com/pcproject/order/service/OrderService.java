package com.pcproject.order.service;

import com.pcproject.admin.dto.LoginAdmin;
import com.pcproject.admin.entity.Admin;
import com.pcproject.customer.entity.Customer;
import com.pcproject.customer.repository.CustomerRepository;
import com.pcproject.order.dto.*;
import com.pcproject.global.exception.CustomException;
import com.pcproject.global.exception.ErrorCode;
import com.pcproject.order.entity.Order;
import com.pcproject.order.entity.OrderStatus;
import com.pcproject.order.repository.OrderRepository;
import com.pcproject.order.repository.OrderSpecification;
import com.pcproject.pcproduct.entity.Product;
import com.pcproject.pcproduct.repository.ProductRepository;
import jakarta.validation.Valid;
import com.pcproject.pcproduct.entity.ProductStatus;
import com.pcproject.pcproduct.repository.ProductRepository;
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
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;


    // 주문 생성(POST)
    @Transactional
    public CreateOrderResponse createOrder(
            CreateOrderRequest request,
            Admin admin) {

        // 고객 조회
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(()-> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        // 상품 조회 (비관적 락 적용: 재고 동시성 제어)
//        Product product = productRepository.findByIdForUpdate(request.getProductId())
//  ProductRepository 랑 통일
        Product product = productRepository.findWithLockByIdAndDeletedAtIsNull(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 판매 가능 상태 및 삭제 여부 검증
        // 재고 부족 여부를 도메인 내부에서 검증 후 차감
        product.validateOrderable();
        product.decreaseStock(request.getQuantity());

        // 주문번호(임시) ORD-생성시간-랜덤
        String orderNumber =
                "ORD-" + System.currentTimeMillis()
                + "-" + UUID.randomUUID().toString().substring(0, 4);

        // 상품 가격(임시)
        //Long unitPrice = 10000L;
//        Customer customer = customerRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
// 동일 코드로 인한 에러 수정
        // 상품 가격 스냅샷
        Long unitPrice = product.getPrice();

        // 주문 생성 (로그인 관리자 정보 포함)
        Order order = new Order(
                orderNumber,
                customer,
                product,
                admin,
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

    // 주문 상태 변경(PATCH)
    @Transactional
    public UpdateOrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderRequest request,
            Admin admin) {

        // 주문 존재 여부 검증
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 상태 전이 검증 및 변경은 도메인 정책에 따라 엔티티에서 처리
        order.changeStatus(request.getStatus());

        return new UpdateOrderResponse(
                order.getId(),
                order.getStatus(),
                order.getUpdatedAt()
        );
    }

    // 주문 취소(PATCH)
    @Transactional
    public CancelOrderResponse cancelOrder(
            Long orderId,
            CancelOrderRequest request,
            Admin admin) {

        // 주문 존재 여부 검증
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
                //.orElseThrow(() -> new IllegalStateException("ORDER_NOT_FOUND"));


//        // 2) PREPARING만 취소 가능(추후 공통 에러 코드로 변경 예정)
//        if (order.getStatus() != OrderStatus.PREPARING) {
//            //throw new IllegalStateException("ORDER_CANCEL_NOT_ALLOWED");
//            throw new CustomException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
//        }

        // 주문 취소 정책은 Order 도메인에서 처리
        order.cancel(request.getCancelReason());

        // 취소 수량만큼 재고 복구 (Product 도메인 정책 적용)
        Product product = order.getProduct();
        product.restoreStock(order.getQuantity());

        return new CancelOrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCancelReason(),
                order.getUpdatedAt()
        );
    }

// 주문 목록 조회, 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderListResponse getOrders(OrderSearchRequest request) {
        String keyword = request.getKeyword();
        OrderStatus status = request.getStatus();
        int page = request.getPage();
        int size = request.getSize();
        String sortBy = request.getSortBy();
        String direction = request.getDirection();

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Specification<Order> spec = Specification
                .where(OrderSpecification.searchByKeyword(keyword))
                .and(OrderSpecification.filterByStatus(status))
                .and(OrderSpecification.fetchAssociations());

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
