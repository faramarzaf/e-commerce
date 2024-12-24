package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.CustomerResponseDto;
import org.example.ecommerce.entity.Customer;

public interface CustomerMapper {

    CustomerResponseDto toCustomerResponseDto(Customer customer);

    Customer toCustomer(CustomerResponseDto customerResponseDto);

}
