package com.pcproject.order.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 2. 20.
 * Time: 오후 8:07
 **/

@Getter
@NoArgsConstructor
public class CreateOrderRequest {

    @NotNull
    private Long customerId;

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    // 요청 or 인증 추출
    // private Long adminId;

}
