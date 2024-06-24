package com.example.JWTSecurity.exceptionHandling;

import com.example.JWTSecurity.exceptionHandling.exceptions.EmailServiceException;
import com.example.JWTSecurity.exceptionHandling.exceptions.RegistrationLinkExpiredException;
import com.example.JWTSecurity.exceptionHandling.exceptions.UserAlreadyActiveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(RegistrationLinkExpiredException.class)
    public ResponseEntity<String> handle(RegistrationLinkExpiredException exception){
        return ResponseEntity.badRequest().body("The registration link has expired");
    }
    @ExceptionHandler(UserAlreadyActiveException.class)
    public ResponseEntity<String> handle(UserAlreadyActiveException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    @ExceptionHandler(EmailServiceException.class)
    public ResponseEntity<String> handle(EmailServiceException exception){
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }
}
