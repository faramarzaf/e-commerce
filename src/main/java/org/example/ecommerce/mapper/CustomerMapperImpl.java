package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.CustomerResponseDto;
import org.example.ecommerce.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerResponseDto toCustomerResponseDto(Customer customer) {
        return new CustomerResponseDto(customer.getId(), customer.getUsername(), customer.getEmail(), customer.getPhoneNumber(), customer.getAddress(), customer.getRole());
    }

    @Override
    public Customer toCustomer(CustomerResponseDto customerResponseDto) {
        return new Customer(customerResponseDto.id(),
                customerResponseDto.username(), customerResponseDto.email(), null, customerResponseDto.phoneNumber(),
                customerResponseDto.address(), customerResponseDto.role(), null, null);
    }

}
