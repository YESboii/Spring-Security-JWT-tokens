package com.example.JWTSecurity.Service;

import com.example.JWTSecurity.Model.Token;
import com.example.JWTSecurity.Repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    //Transactional is used for logout
    @Transactional(readOnly = false)
    public void revokeAllTokensForUserId(Integer id){
        List<Token> listOfTokensToBeRevoked = tokenRepository.findTokensByUserIdNotRevoked(id);
        listOfTokensToBeRevoked.forEach(u -> u.setRevoked(true));
    }


    public void saveTokensOnLogin(List<Token> tokens){
        tokenRepository.saveAll(tokens);
    }


}
