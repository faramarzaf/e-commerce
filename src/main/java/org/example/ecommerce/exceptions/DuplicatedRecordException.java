package org.example.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicatedRecordException extends CustomException {

    public DuplicatedRecordException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }

}
