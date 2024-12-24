package org.example.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.dto.OrderResponseDto;
import org.example.ecommerce.entity.CustomerTransaction;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.enums.TransactionStatus;
import org.example.ecommerce.mapper.OrderMapper;
import org.example.ecommerce.repo.CustomerTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final CustomerTransactionRepository transactionRepository;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Transactional
    public CustomerTransaction processPayment(Long orderId, boolean paymentSuccess) {
        String trackingCode = UUID.randomUUID().toString();

        OrderResponseDto orderResponseDto = orderService.getOrderById(orderId);
        Order order = orderMapper.toOrder(orderResponseDto);

        CustomerTransaction transaction = new CustomerTransaction();
        transaction.setOrder(order);
        transaction.setAmount(order.getTotalPrice());

        if (paymentSuccess) {
            transaction.setStatus(TransactionStatus.SUCCESS);
            orderService.completeOrder(orderId);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            orderService.cancelOrder(orderId);
        }
        transaction.setTrackingCode(trackingCode);
        return transactionRepository.save(transaction);
    }
}
