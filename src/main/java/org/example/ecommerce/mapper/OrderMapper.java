package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.OrderResponseDto;
import org.example.ecommerce.entity.Order;

public interface OrderMapper {

    OrderResponseDto toOrderResponseDto(Order order);

    Order toOrder(OrderResponseDto orderResponseDto);

}
