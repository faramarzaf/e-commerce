package org.example.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class AuthException extends CustomException {
    public AuthException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }


}
