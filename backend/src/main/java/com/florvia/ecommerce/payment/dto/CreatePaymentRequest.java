package com.florvia.ecommerce.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private String provider;
}
