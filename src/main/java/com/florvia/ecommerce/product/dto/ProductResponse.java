package com.florvia.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private Integer stock;
    private String status;
    private String categoryName;
}
