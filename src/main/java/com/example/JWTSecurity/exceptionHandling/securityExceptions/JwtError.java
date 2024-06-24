package com.example.JWTSecurity.exceptionHandling.securityExceptions;

public record JwtError(String requestUri, int status, String description, String redirect) {
}
