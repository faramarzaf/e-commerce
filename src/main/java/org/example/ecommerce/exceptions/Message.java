package org.example.ecommerce.exceptions;

import lombok.Data;

@Data
public class Message {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}
