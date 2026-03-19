package com.florvia.ecommerce.order.mapper;

import com.florvia.ecommerce.entity.Order;
import com.florvia.ecommerce.entity.OrderItem;
import com.florvia.ecommerce.order.dto.OrderResponse;
import com.florvia.ecommerce.order.dto.OrderItemResponse;
import org.mapstruct.Mapper;
import com.florvia.ecommerce.order.dto.CheckoutResponse;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);
    OrderItemResponse toItemResponse(OrderItem item);

    @Mapping(target = "orderId", source = "id")
    CheckoutResponse toCheckoutResponse(Order order);
}