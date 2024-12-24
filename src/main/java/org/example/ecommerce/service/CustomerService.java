package org.example.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.dto.CustomerLoginDto;
import org.example.ecommerce.dto.CustomerRegisterDto;
import org.example.ecommerce.dto.CustomerResponseDto;
import org.example.ecommerce.dto.CustomerUpdateRequestDto;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.enums.Role;
import org.example.ecommerce.exceptions.AuthException;
import org.example.ecommerce.exceptions.DuplicatedRecordException;
import org.example.ecommerce.exceptions.NotFoundException;
import org.example.ecommerce.exceptions.ValidationException;
import org.example.ecommerce.mapper.CustomerMapper;
import org.example.ecommerce.repo.CustomerRepository;
import org.example.ecommerce.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginAttemptService loginAttemptService;
    private final CustomerMapper customerMapper;

    public CustomerResponseDto getProfile(String username) {
        return customerMapper.toCustomerResponseDto(customerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Transactional
    public CustomerResponseDto updateProfile(CustomerUpdateRequestDto requestDto) {
        Customer existingCustomer = customerRepository.findByUsername(requestDto.username())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!ObjectUtils.isEmpty(requestDto.phoneNumber()))
            existingCustomer.setPhoneNumber(requestDto.phoneNumber());

        if (!ObjectUtils.isEmpty(requestDto.email()))
            existingCustomer.setEmail(requestDto.email());

        if (!ObjectUtils.isEmpty(requestDto.address()))
            existingCustomer.setAddress(requestDto.address());

        return customerMapper.toCustomerResponseDto(customerRepository.save(existingCustomer));
    }

    public String login(CustomerLoginDto loginDto) {
        String email = loginDto.email();

        if (loginAttemptService.isBlocked(email)) {
            throw new AuthException("Too many failed login attempts. Please try again later.");
        }
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Invalid email or password."));

        if (!passwordEncoder.matches(loginDto.password(), customer.getPassword())) {
            loginAttemptService.loginFailed(email); // Record failed attempt
            throw new AuthException("Invalid email or password.");
        }

        loginAttemptService.loginSucceeded(email);
        return jwtTokenProvider.generateToken(customer.getUsername());

    }

    @Transactional
    public CustomerResponseDto register(CustomerRegisterDto registerDto) {
        if (ObjectUtils.isEmpty(registerDto.email())) {
            throw new ValidationException("Email is required.");
        }
        if (ObjectUtils.isEmpty(registerDto.password())) {
            throw new ValidationException("Password must be at least 6 characters.");
        }

        if (customerRepository.existsByEmail(registerDto.email())) {
            throw new DuplicatedRecordException("Email is already in use.");
        }

        String encodedPassword = passwordEncoder.encode(registerDto.password());

        Customer newCustomer = new Customer();
        newCustomer.setUsername(registerDto.username());
        newCustomer.setEmail(registerDto.email());
        newCustomer.setPassword(encodedPassword);
        newCustomer.setAddress(registerDto.address());
        newCustomer.setPhoneNumber(registerDto.phoneNumber());
        newCustomer.setRole(Role.USER);
        Customer savedCustomer = customerRepository.save(newCustomer);
        return customerMapper.toCustomerResponseDto(savedCustomer);
    }


}
