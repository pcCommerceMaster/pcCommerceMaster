package com.pcproject.order.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * User: jeongjihun
 * Date: 26. 2. 23.
 * Time: 오후 3:34
 **/

@Getter
public class CancelOrderRequest {

    @NotBlank
    private String cancelReason;
}
