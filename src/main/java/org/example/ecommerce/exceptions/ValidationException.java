package org.example.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends CustomException {

    public ValidationException(String message) {
        super(message, HttpStatus.NOT_ACCEPTABLE.value());
    }

}
