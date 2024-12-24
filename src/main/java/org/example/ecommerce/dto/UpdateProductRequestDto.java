package org.example.ecommerce.dto;

public record UpdateProductRequestDto(long id, String name, double price, int stock, String category,
                                      String description) {
}
