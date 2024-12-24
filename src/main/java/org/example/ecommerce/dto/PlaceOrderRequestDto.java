package org.example.ecommerce.dto;

import java.util.List;

public record PlaceOrderRequestDto(Long customerId, List<Long> productIds, int quantity) {
}
