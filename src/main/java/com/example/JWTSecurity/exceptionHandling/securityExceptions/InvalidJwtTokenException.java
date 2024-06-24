package com.example.JWTSecurity.exceptionHandling.securityExceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtTokenException extends AuthenticationException {
    public InvalidJwtTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidJwtTokenException(String msg) {
        super(msg);
    }
}
