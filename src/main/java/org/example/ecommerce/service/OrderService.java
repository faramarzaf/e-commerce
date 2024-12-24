package org.example.ecommerce.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.dto.OrderResponseDto;
import org.example.ecommerce.dto.PlaceOrderRequestDto;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.OrderProduct;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.enums.OrderStatus;
import org.example.ecommerce.exceptions.NotFoundException;
import org.example.ecommerce.exceptions.ValidationException;
import org.example.ecommerce.mapper.OrderMapper;
import org.example.ecommerce.repo.CustomerRepository;
import org.example.ecommerce.repo.OrderProductRepository;
import org.example.ecommerce.repo.OrderRepository;
import org.example.ecommerce.repo.ProductRepository;
import org.example.ecommerce.service.event.OrderEventHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderEventHandler orderEventHandler;


    public OrderResponseDto getOrderById(Long orderId) {
        return orderMapper.toOrderResponseDto(orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found")));
    }

    @Transactional
    public OrderResponseDto placeOrder(PlaceOrderRequestDto requestDto) {
        Customer customer = customerRepository.findById(requestDto.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + requestDto.customerId()));

        List<Product> productsById = Optional.of(productRepository.findAllById(requestDto.productIds()))
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + requestDto.productIds()));


        int quantity = requestDto.quantity();
        double totalPrice = 0;
        List<OrderProduct> orderProducts = new ArrayList<>();

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        for (Product product : productsById) {
            boolean notEnoughProducts = product.getStockQuantity() < quantity;
            if (notEnoughProducts)
                throw new ValidationException("Insufficient stock for product: " + product.getName());

            totalPrice += product.getPrice() * quantity;

            updateProductQuantity(product, quantity);

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(quantity);
            orderProduct.setPriceAtPurchase(product.getPrice() * quantity);
            orderProducts.add(orderProduct);
        }
        order.setProducts(orderProducts.stream().map(OrderProduct::getProduct).toList());
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.saveAndFlush(order);

        for (OrderProduct orderProduct : orderProducts)
            orderProduct.setOrder(savedOrder);

        orderProductRepository.saveAll(orderProducts);
        orderEventHandler.handleOrderPlaced(order.getId());
        return orderMapper.toOrderResponseDto(savedOrder);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        if (!order.getStatus().equals(OrderStatus.PENDING))
            throw new ValidationException("Only pending orders can be canceled");


        List<OrderProduct> orderProducts = orderProductRepository.findByOrder_Id(orderId);

        for (OrderProduct orderProduct : orderProducts) {
            Product product = orderProduct.getProduct();
            product.setStockQuantity(product.getStockQuantity() + orderProduct.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }


    @Transactional
    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        if (!order.getStatus().equals(OrderStatus.PENDING))
            throw new ValidationException("Only pending orders can be completed.");

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }


    private void updateProductQuantity(Product product, int quantity) {
        int remainedQuantity = product.getStockQuantity() - quantity;
        product.setStockQuantity(remainedQuantity);
        productRepository.save(product);
    }
}
