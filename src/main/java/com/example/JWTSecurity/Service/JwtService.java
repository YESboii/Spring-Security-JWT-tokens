package com.example.JWTSecurity.Service;

import com.example.JWTSecurity.Repository.UserRepository;
import com.example.JWTSecurity.security.Exceptions.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.jwt.secretKey}")
    private String secretKey;

    private final UserRepository userRepository;

    @Value("${application.jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails){
        return Jwts
                .builder()
                .addClaims(new HashMap<>())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .setSubject(userDetails.getUsername())
                .compact();
    }
    public boolean verifyToken(String extractedEmail,UserDetails userDetails,String token){
        return !isExpired(token) && extractedEmail.equals(userDetails.getUsername()) ;
    }
    public String extractToken(HttpServletRequest httpRequest){
        String token = httpRequest.getHeader("Authorization");

        if( token!= null && token.length()>=8 && token.startsWith("Bearer")){
            return token.substring(7);
        }
        return null;
    }

    private Key getSignInKey(){
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }
    private Claims extractAllClaims(String token) {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }
    private <R> R extractClaim(String token, Function<Claims,R> claimResolverFunction){
        Claims claims = extractAllClaims(token);
        return claimResolverFunction.apply(claims);
    }
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    private boolean isExpired(String token){
        Date expiredDate = getExpiration(token);
        return expiredDate.before(new Date());
    }
    private Date getExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
}
