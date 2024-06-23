package com.example.JWTSecurity.controllers;

import com.example.JWTSecurity.Service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        String jwt = authenticationService.authenticate(loginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(jwt);
    }
    @GetMapping("/test")
    public ResponseEntity<String>test(){
        return ResponseEntity.ok("This is free");
    }
}
