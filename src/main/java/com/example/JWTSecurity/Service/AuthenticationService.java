package com.example.JWTSecurity.Service;

import com.example.JWTSecurity.Model.Roles.Role;
import com.example.JWTSecurity.Model.User;
import com.example.JWTSecurity.Repository.UserRepository;
import com.example.JWTSecurity.controllers.utils.LoginRequest;
import com.example.JWTSecurity.controllers.utils.RegistrationRequest;
import com.example.JWTSecurity.exceptionHandling.exceptions.RegistrationLinkExpiredException;
import com.example.JWTSecurity.exceptionHandling.exceptions.UserAlreadyActiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public String authenticate(LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password()));
        return jwtService.generateToken((UserDetails)authentication.getPrincipal());
    }

    //make it transactional

    @Transactional
    public User register(RegistrationRequest request){
        User user = User.builder()
                .email(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.MANAGER)
                .isEnabled(false)
                .registrationKey(generateKey())
                .expirationTime(generateExpirationTime())
                .build();
        user = userRepository.save(user);

        emailService.sendRegistrationMessage(user.getRegistrationKey(), user.getUsername());

        return user;
    }

    ///create a new exception and handle it in the restCAdvice
    public User activate(String registrationKey)  {
        User user = userRepository.findByRegistrationKey(registrationKey).orElseThrow(RuntimeException::new);
        LocalDateTime expiration = user.getExpirationTime();

        if (expiration.isBefore(LocalDateTime.now())){
            //expired Link
            //throw Link expired exception handle it in rca
            throw new RegistrationLinkExpiredException("Link expired");

        }
        else if(!user.isEnabled()){
            user.setEnabled(true);
            userRepository.save(user);
            return user;
        }
        else{
            throw new UserAlreadyActiveException("User Already Registered");
        }

    }

    private LocalDateTime generateExpirationTime(){
        return LocalDateTime.now().plusDays(1);
    }
    private String generateKey(){
        return UUID.randomUUID().toString();
    }


}
