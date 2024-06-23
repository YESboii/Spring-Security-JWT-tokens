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
public class JwtSecurityApplication implements CommandLineRunner {


	final UserRepository userRepository;

	final PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setEmail("xyz@gmail.com");
		user.setRole(Role.ADMIN);
		user.setPassword(passwordEncoder.encode("password"));
//		user.setPassword("password");

		User user1 = new User();
		user1.setEmail("abc@gmail.com");
		user1.setRole(Role.MANAGER);
		user1.setPassword(passwordEncoder.encode("password"));
//		user1.setPassword("password");
		userRepository.save(user1);
		userRepository.save(user);

	}
}
