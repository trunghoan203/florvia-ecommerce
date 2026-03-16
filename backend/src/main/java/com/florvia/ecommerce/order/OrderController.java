package com.florvia.ecommerce.order;

import com.florvia.ecommerce.common.ApiResponse;
import com.florvia.ecommerce.order.dto.CheckoutResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
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
}
