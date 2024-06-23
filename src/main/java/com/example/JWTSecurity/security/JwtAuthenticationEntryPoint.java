package com.example.JWTSecurity.security;

import com.example.JWTSecurity.security.Exceptions.InvalidJwtTokenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//configure in Spring Security
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        if(authException instanceof InvalidJwtTokenException){
            response.getWriter().write("invalid token");
        }
        else {
            response.getWriter().write("UnAuthorised");
        }
    }
}
