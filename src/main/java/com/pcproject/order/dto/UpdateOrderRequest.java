package com.pcproject.order.dto;


import com.pcproject.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 2. 23.
 * Time: 오전 10:44
 **/

@Getter
public class UpdateOrderRequest {

    @NotNull
    private OrderStatus status;
}
