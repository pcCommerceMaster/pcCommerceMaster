package com.pcproject.customer.service;


import com.pc_commerce_master.global.exception.CustomException;
import com.pc_commerce_master.global.exception.ErrorCode;
import com.pcproject.customer.dto.*;
import com.pcproject.customer.entity.Customer;
import com.pcproject.customer.entity.CustomerStatus;
import com.pcproject.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 리스트 조회
    public GetCustomerListResponse getCustomers(String keyword, Pageable pageable) {
        Page<Customer> page = customerRepository.
                findByNameContainingAndDeletedAtIsNullOrEmailContainingAndDeletedAtIsNull(keyword, keyword, pageable);
        // 이름과 email로 검색하되 삭제되지 않은 정보만 탐색

        List<GetCustomerResponse> customers = page.getContent().stream()
                .map(c -> new GetCustomerResponse(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getPhoneNumber(),
                        c.getStatus().name(),
                        c.getCreatedAt()))
                .toList();

        return new GetCustomerListResponse(customers,
                page.getNumber() + 1,
                page.getTotalElements(),
                page.getTotalPages());
    }

    // 상세 조회
    public GetCustomerResponse getCustomer(Long id) {
        Customer customer = findById(id);
        return new GetCustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getStatus().name(),
                customer.getCreatedAt());
    }

    // 고객이 존재하지 않을 때 예외 처리
    private Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    // 정보 수정
    @Transactional
    public UpdateCustomerInfoResponse updateInfo(Long id, UpdateCustomerInfoRequest request) {
        Customer customer = findById(id);

        if (!customer.getEmail().equals(request.getEmail()) &&
                customerRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        customer.updateInfo(request.getName(), request.getEmail(), request.getPhoneNumber());

        return new UpdateCustomerInfoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                LocalDateTime.now());
    }

    // 상태 변경
    @Transactional
    public UpdateCustomerStatusResponse updateStatus(Long id, UpdateCustomerStatusRequest request) {
        Customer customer = findById(id);

        //
        try {
            customer.changeStatus(CustomerStatus.valueOf(request.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        return new UpdateCustomerStatusResponse(
                customer.getId(),
                customer.getStatus().name(),
                LocalDateTime.now());
    }

    // soft delete
    @Transactional
    public void deleteCustomer(Long id) {
        // 레포지토리에서 미삭제 고객 검색
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        customer.softDelete();
    }
}
