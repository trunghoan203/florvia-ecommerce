package com.florvia.ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {

    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
}
