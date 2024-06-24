package com.example.JWTSecurity.exceptionHandling.exceptions;

public class RegistrationLinkExpiredException extends RuntimeException{

    public RegistrationLinkExpiredException(String msg){
        super(msg);
    }
}
