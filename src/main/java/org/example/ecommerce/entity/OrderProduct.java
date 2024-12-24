package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity; // Number of this product in the order

    private double priceAtPurchase; // Price of the product at the time of order
}
