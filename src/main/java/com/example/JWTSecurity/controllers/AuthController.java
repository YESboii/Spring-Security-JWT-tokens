package com.example.JWTSecurity.controllers;

import com.example.JWTSecurity.Model.User;
import com.example.JWTSecurity.Service.AuthenticationService;
import com.example.JWTSecurity.controllers.utils.LoginRequest;
import com.example.JWTSecurity.controllers.utils.RegistrationRequest;
import com.example.JWTSecurity.controllers.utils.RegistrationResponse;
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
    @PostMapping("register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request){
        User user = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationResponse(user, "Please Check your email"));
    }
    @PostMapping("register/{registrationKey}")
    public ResponseEntity<RegistrationResponse> activate(@PathVariable String registrationKey) {
        System.out.println(registrationKey);
        User user = authenticationService.activate(registrationKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationResponse(user, "Activated Successfully"));
    }

    @GetMapping("/test")
    public ResponseEntity<String>test(){
        return ResponseEntity.ok("This is free");
    }
}
