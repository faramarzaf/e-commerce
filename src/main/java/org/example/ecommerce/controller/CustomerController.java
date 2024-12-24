package org.example.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.*;
import org.example.ecommerce.service.CustomerService;
import org.example.ecommerce.utils.MyUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseDto<CustomerResponseDto> registerCustomer(@RequestBody CustomerRegisterDto registerDto) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, customerService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseDto<String> loginCustomer(@RequestBody CustomerLoginDto loginDto) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, customerService.login(loginDto));
    }

    @PutMapping("/update")
    public ResponseDto<CustomerResponseDto> updateCustomer(@RequestBody CustomerUpdateRequestDto registerDto) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, customerService.updateProfile(registerDto));
    }

    @GetMapping("/profile/{username}")
    public ResponseDto<CustomerResponseDto> getCustomerProfile(@PathVariable String username) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, customerService.getProfile(username));
    }

}
