package com.example.JWTSecurity.controllers;

import com.example.JWTSecurity.Model.Token;
import com.example.JWTSecurity.Repository.TokenRepository;
import com.example.JWTSecurity.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("You this is private");
    }
}
