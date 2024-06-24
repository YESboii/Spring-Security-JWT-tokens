package com.example.JWTSecurity.exceptionHandling.exceptions;

import lombok.NoArgsConstructor;


public class EmailServiceException extends RuntimeException{
    public EmailServiceException() {
    }

    public EmailServiceException(String message) {
        super(message);
    }
}
