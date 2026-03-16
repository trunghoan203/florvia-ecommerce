package com.florvia.ecommerce.cart;

import com.florvia.ecommerce.cart.dto.AddToCartRequest;
import com.florvia.ecommerce.cart.dto.CartItemResponse;
import com.florvia.ecommerce.cart.dto.CartResponse;
import com.florvia.ecommerce.entity.*;
import com.florvia.ecommerce.exception.ResourceNotFoundException;
import com.florvia.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartResponse getMyCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserAndStatus(user, "ACTIVE")
                .orElseGet(() -> createNewCart(user));

        return mapToResponse(cart);
    }

    public void addToCart(String email, AddToCartRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserAndStatus(user, "ACTIVE")
                .orElseGet(() -> createNewCart(user));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
        } else {
            item.setQuantity(item.getQuantity() + request.getQuantity());
        }

        cartItemRepository.save(item);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus("ACTIVE");
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    private CartResponse mapToResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItems().stream()
                .map(i -> new CartItemResponse(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getProduct().getPrice(),
                        i.getQuantity()
                ))
                .toList();

        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        return new CartResponse(cart.getId(), items, total);
    }
}
