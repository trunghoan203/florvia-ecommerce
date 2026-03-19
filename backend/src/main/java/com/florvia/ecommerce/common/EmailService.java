package com.florvia.ecommerce.common;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendOrderConfirmation(String to, String orderId, double total) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Order Confirmation - Florvia Ecommerce");
        message.setText("Thank you for your order!\n\nOrder ID: " + orderId + "\nTotal Amount: $" + total + "\nStatus: Pending Payment");

        mailSender.send(message);
    }
}