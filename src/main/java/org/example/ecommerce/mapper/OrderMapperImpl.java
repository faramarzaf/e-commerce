package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.CustomerResponseDto;
import org.example.ecommerce.dto.OrderResponseDto;
import org.example.ecommerce.entity.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderMapperImpl implements OrderMapper {

    private final CustomerMapper customerMapper;

    public OrderMapperImpl(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @Override
    public OrderResponseDto toOrderResponseDto(Order order) {
        CustomerResponseDto customerResponseDto = customerMapper.toCustomerResponseDto(order.getCustomer());
        return new OrderResponseDto(order.getId(), order.getProducts(), customerResponseDto, order.getTotalPrice(), order.getStatus(), order.getCreatedAt());
    }

    @Override
    public Order toOrder(OrderResponseDto orderResponseDto) {
        return new Order(orderResponseDto.id(), customerMapper.toCustomer(orderResponseDto.customerResponseDto()), orderResponseDto.products(), orderResponseDto.totalPrice(), orderResponseDto.status(), orderResponseDto.createdAt());
    }

}
