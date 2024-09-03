package com.example.JWTSecurity.security;

import com.example.JWTSecurity.Model.Token;
import com.example.JWTSecurity.Repository.TokenRepository;
import com.example.JWTSecurity.Service.JwtService;
import com.example.JWTSecurity.Service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private final TokenService tokenService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String bearerToken = jwtService.extractToken(request);
        if( bearerToken== null){
            return;
        }
            Integer id = null;
            try {
                id = jwtService.extractId(bearerToken);
            }catch (ExpiredJwtException  |MalformedJwtException | SignatureException e){
                System.out.println(e.toString());
                return;
            }
            tokenService.revokeAllTokensForUserId(id);

    }
}
