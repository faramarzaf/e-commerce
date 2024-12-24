package org.example.ecommerce.dto;

import org.example.ecommerce.enums.Role;

public record CustomerResponseDto(Long id, String username, String email, String phoneNumber, String address,
                                  Role role) {
}
