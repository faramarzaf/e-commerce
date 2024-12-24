package org.example.ecommerce.exceptions;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final int statusCode;

    public CustomException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
