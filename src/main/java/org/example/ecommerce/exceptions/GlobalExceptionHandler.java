package org.example.ecommerce.exceptions;

import org.example.ecommerce.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<Void>> handleCustomException(CustomException ex) {
        ResponseDto<Void> response = ResponseDto.failure(ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleGeneralException(Exception ex) {
        ResponseDto<Void> response = ResponseDto.failure("An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
