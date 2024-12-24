package org.example.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.ResponseDto;
import org.example.ecommerce.entity.CustomerTransaction;
import org.example.ecommerce.service.TransactionService;
import org.example.ecommerce.utils.MyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{orderId}/{paymentSuccess}")
    public ResponseDto<CustomerTransaction> processPayment(@PathVariable Long orderId, @PathVariable boolean paymentSuccess) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, transactionService.processPayment(orderId, paymentSuccess));
    }

}
