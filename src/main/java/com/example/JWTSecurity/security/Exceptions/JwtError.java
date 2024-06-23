package com.example.JWTSecurity.security.Exceptions;

public record JwtError(String requestUri, int status, String description, String redirect) {
}
