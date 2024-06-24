package com.example.JWTSecurity.exceptionHandling.securityExceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class JwtErrorHandler {
    public static  void handle(HttpServletRequest request, HttpServletResponse response,Exception exception) throws IOException {
        JwtError jwtError;

        if (exception instanceof ExpiredJwtException) {
            jwtError = new JwtError(request.getRequestURI(), HttpStatus.UNAUTHORIZED.value(),
                    "The JWT token has expired", "/login");
        } else if (exception instanceof SignatureException) {
            jwtError = new JwtError(request.getRequestURI(), HttpStatus.FORBIDDEN.value(),
                    "The JWT signature is invalid", "/login");
        } else if (exception instanceof MalformedJwtException) {
            jwtError = new JwtError(request.getRequestURI(), HttpStatus.FORBIDDEN.value(),
                    "The JWT signature is malformed", "/login");
        } else {
            System.out.println(exception.getClass());
            jwtError = new JwtError(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Something went wrong", "/login");
        }

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(jwtError);

        response.getWriter().write(body);
        response.setStatus(jwtError.status());
    }
}

