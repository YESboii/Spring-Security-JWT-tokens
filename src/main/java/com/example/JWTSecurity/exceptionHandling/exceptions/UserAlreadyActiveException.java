package com.example.JWTSecurity.exceptionHandling.exceptions;

public class UserAlreadyActiveException extends RuntimeException{
    public UserAlreadyActiveException(String message){
        super(message);
    }
}
