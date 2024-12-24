package org.example.ecommerce.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.exceptions.ValidationException;
import org.example.ecommerce.service.TransactionService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderMessageListener {

    private final TransactionService transactionService;

    @JmsListener(destination = "order.queue")
    public void receiveOrderMessage(String message) {
        log.info("Received message: {}", message);
        try {
            Long orderId = extractOrderIdFromMessage(message);
            transactionService.processPayment(orderId, true);
            log.info("Order ID: {} successfully completed.", orderId);
        } catch (Exception e) {
            log.error("Failed to complete order: {}", e.getMessage());
        }
    }

    private Long extractOrderIdFromMessage(String message) {
        try {
            return Long.parseLong(message.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid order ID in message: " + message);
        }
    }
}
