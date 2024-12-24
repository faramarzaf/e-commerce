package org.example.ecommerce.dto;

import org.example.ecommerce.entity.Product;
import org.example.ecommerce.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(Long id, List<Product> products, CustomerResponseDto customerResponseDto,
                               double totalPrice, OrderStatus status, LocalDateTime createdAt) {
}
