package com.example.JWTSecurity.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

//configure in Spring Security
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        System.out.println(authException.getClass());
        authException.printStackTrace();
        if (authException instanceof DisabledException){
            response.getWriter().write("Please check your email ! to activate your account");
        } else if (authException instanceof InsufficientAuthenticationException) {
            response.getWriter().write("Requires Login");
        }

        else{
            response.getWriter().write("UnAuthorised");
        }
    }
}
