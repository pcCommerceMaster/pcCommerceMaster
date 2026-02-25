package com.pcproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 관리자
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다."),
    ADMIN_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    ADMIN_INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    ADMIN_STATUS_PENDING(HttpStatus.FORBIDDEN, "승인 대기 중인 계정입니다."),
    ADMIN_STATUS_REJECTED(HttpStatus.FORBIDDEN, "거부된 계정입니다."),
    ADMIN_STATUS_SUSPENDED(HttpStatus.FORBIDDEN, "정지된 계정입니다."),
    ADMIN_STATUS_INACTIVE(HttpStatus.FORBIDDEN, "비활성화된 계정입니다."),

    // 고객
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 고객입니다."),

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    PRODUCT_DISCONTINUED(HttpStatus.CONFLICT, "단종된 상품입니다."),
    PRODUCT_SOLD_OUT(HttpStatus.CONFLICT, "품절된 상품입니다."),
    PRODUCT_STOCK_INSUFFICIENT(HttpStatus.CONFLICT, "재고가 부족합니다."),
    PRODUCT_NOT_ON_SALE(HttpStatus.CONFLICT, "판매 중인 상품이 아닙니다."),
    PRODUCT_STATUS_CONFLICT(HttpStatus.CONFLICT, "허용되지 않은 상태 전이입니다."),
    PRODUCT_DISCONTINUED_CONFLICT(HttpStatus.CONFLICT, "단종 상태는 변경할 수 없습니다."),
    PRODUCT_NOT_DELETED(HttpStatus.BAD_REQUEST, "삭제되지 않은 상품은 복구할 수 없습니다."),

    // 주문
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    ORDER_CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "준비중 상태의 주문만 취소할 수 있습니다."),
    ORDER_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다.");

    private final HttpStatus status;
    private final String message;
}