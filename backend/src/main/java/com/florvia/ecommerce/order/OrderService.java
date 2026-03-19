package com.florvia.ecommerce.order;

import com.florvia.ecommerce.common.EmailService;
import com.florvia.ecommerce.entity.*;
import com.florvia.ecommerce.exception.BadRequestException;
import com.florvia.ecommerce.exception.ResourceNotFoundException;
import com.florvia.ecommerce.order.dto.CheckoutResponse;
import com.florvia.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.florvia.ecommerce.order.dto.OrderResponse;
import com.florvia.ecommerce.order.mapper.OrderMapper;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final EmailService emailService;

    public Page<OrderResponse> getMyOrders(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser(user, pageable)
                .map(orderMapper::toResponse);
    }

    @Transactional
    public CheckoutResponse checkout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserAndStatus(user, "ACTIVE")
                .orElseThrow(() -> new BadRequestException("Cart is empty"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING_PAYMENT");
        order.setItems(new ArrayList<>());

        double total = 0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Product " + product.getName() + " is out of stock");
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            order.getItems().add(orderItem);
            total += product.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        cart.setStatus("CHECKED_OUT");
        cartRepository.save(cart);

        emailService.sendOrderConfirmation(user.getEmail(), order.getId().toString(), order.getTotalAmount());

        return orderMapper.toCheckoutResponse(order);
    }
}
