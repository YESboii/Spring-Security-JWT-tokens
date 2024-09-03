package com.example.JWTSecurity.Model;

import com.example.JWTSecurity.Model.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(indexes = {@Index(columnList = "jwt",name = "jwt_index")})
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String jwt;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;



    private boolean isRevoked;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private User user;

    public Token() {

    }
}
