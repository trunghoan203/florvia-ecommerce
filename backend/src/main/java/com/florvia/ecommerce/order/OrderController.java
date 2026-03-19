package com.florvia.ecommerce.order;

import com.florvia.ecommerce.common.ApiResponse;
import com.florvia.ecommerce.order.dto.CheckoutResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import com.florvia.ecommerce.order.dto.OrderResponse;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ApiResponse<CheckoutResponse> checkout(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ApiResponse.success(
                "Checkout success",
                orderService.checkout(userDetails.getUsername())
        );
    }

    @GetMapping("/my-orders")
    public ApiResponse<Page<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ApiResponse.success(
                "Fetch order history successfully",
                orderService.getMyOrders(userDetails.getUsername(), pageable)
        );
    }
}
