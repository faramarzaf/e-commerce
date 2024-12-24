package org.example.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.OrderResponseDto;
import org.example.ecommerce.dto.PlaceOrderRequestDto;
import org.example.ecommerce.dto.ResponseDto;
import org.example.ecommerce.service.OrderService;
import org.example.ecommerce.utils.MyUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseDto<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, orderService.getOrderById(orderId));
    }

    @PostMapping("/placeOrder")
    public ResponseDto<OrderResponseDto> placeOrder(@RequestBody PlaceOrderRequestDto requestDto) {
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, orderService.placeOrder(requestDto));
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseDto<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseDto.success(MyUtils.MESSAGE_SUCCESS, "Order canceled successfully");
    }

}
