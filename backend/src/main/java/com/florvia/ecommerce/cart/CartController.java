package com.florvia.ecommerce.cart;

import com.florvia.ecommerce.cart.dto.AddToCartRequest;
import com.florvia.ecommerce.cart.dto.CartResponse;
import com.florvia.ecommerce.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ApiResponse<CartResponse> getMyCart(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ApiResponse.success(
                "Get cart success",
                cartService.getMyCart(userDetails.getUsername())
        );
    }

    @PostMapping("/items")
    public ApiResponse<Void> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddToCartRequest request) {

        cartService.addToCart(userDetails.getUsername(), request);
        return ApiResponse.success("Add to cart success", null);
    }
}
