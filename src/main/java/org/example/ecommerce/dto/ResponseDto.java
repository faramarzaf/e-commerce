package org.example.ecommerce.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDto<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ResponseDto(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(true, message, data);
    }

    public static <T> ResponseDto<T> failure(String message) {
        return new ResponseDto<>(false, message, null);
    }

}
