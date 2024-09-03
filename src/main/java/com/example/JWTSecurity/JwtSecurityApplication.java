package com.example.JWTSecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class JwtSecurityApplication {



	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityApplication.class, args);
	}


}
