package com.example.JWTSecurity.Service;

import com.example.JWTSecurity.Model.Registration;
import com.example.JWTSecurity.Model.Token;
import com.example.JWTSecurity.Model.enums.Role;
import com.example.JWTSecurity.Model.User;
import com.example.JWTSecurity.Model.enums.TokenType;
import com.example.JWTSecurity.Repository.RegistrationRepository;
import com.example.JWTSecurity.Repository.TokenRepository;
import com.example.JWTSecurity.Repository.UserRepository;
import com.example.JWTSecurity.controllers.utils.LoginRequest;
import com.example.JWTSecurity.controllers.utils.LoginResponse;
import com.example.JWTSecurity.controllers.utils.RegistrationRequest;
import com.example.JWTSecurity.exceptionHandling.exceptions.RegistrationLinkExpiredException;
import com.example.JWTSecurity.exceptionHandling.exceptions.UserAlreadyActiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final RegistrationRepository registrationRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    private final EmailService emailService;


    public LoginResponse authenticate(LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password()));
        User user = (User)authentication.getPrincipal();
        String accessToken =  jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Token access = tokenBuilder(accessToken,user,TokenType.ACCESS);
        Token refresh = tokenBuilder(refreshToken,user,TokenType.REFRESH);
        tokenService.revokeAllTokensForUserId(user.getId());
        tokenService.saveTokensOnLogin(List.of(access,refresh));
        return new LoginResponse(accessToken,refreshToken);
    }

    private Token tokenBuilder(String jwt, User user, TokenType tokenType){
        return Token.builder()
                .jwt(jwt)
                .tokenType(tokenType)
                .isRevoked(false)
                .user(user)
                .build();
    }

    //make it transactional

    @Transactional
    public Optional<User> register(RegistrationRequest request){

        Optional<User> userIfExists = userRepository.findByEmail(request.username());

        if(userIfExists.isPresent()){
            return Optional.empty();
        }

        User user = User.builder()
                .email(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.MANAGER)
                .isEnabled(false)
//                .registrationKey(generateKey())
//                .expirationTime(generateExpirationTime())
                .build();
        String registrationKey = generateRegistrationKey();
        user = userRepository.save(user);
        Registration toBeSaved = Registration.builder()
                .expiration(generateExpirationTime())
                .registrationKey(registrationKey)
                .user(user)
                .build();
        Registration registration = registrationRepository.save(toBeSaved);
        emailService.sendRegistrationMessage(registration.getRegistrationKey(), user.getUsername());

        return Optional.of(user);
    }

    ///create a new exception and handle it in the restCAdvice
    public User activate(String registrationKey)  {
        Registration registration = registrationRepository.findRegistrationByRegistrationKey(registrationKey)
                .orElseThrow(RuntimeException::new); // throw a diff exception
        LocalDateTime expiration = registration.getExpiration();
        User user = registration.getUser();
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
    private String generateRegistrationKey(){
        return UUID.randomUUID().toString();
    }


}
