package org.example.ecommerce.dto;

public record AddProductDto(String name, String description, double price, int stockQuantity, String category) {
}
