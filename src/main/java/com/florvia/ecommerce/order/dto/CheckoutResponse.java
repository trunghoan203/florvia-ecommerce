package com.florvia.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutResponse {
    private Long orderId;
    private Double totalAmount;
    private String status;
}
