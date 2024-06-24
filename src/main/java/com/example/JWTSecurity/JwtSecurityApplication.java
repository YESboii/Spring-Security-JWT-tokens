package com.example.JWTSecurity;

import com.example.JWTSecurity.Model.Roles.Role;
import com.example.JWTSecurity.Model.User;
import com.example.JWTSecurity.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class JwtSecurityApplication {



	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityApplication.class, args);
	}


}
