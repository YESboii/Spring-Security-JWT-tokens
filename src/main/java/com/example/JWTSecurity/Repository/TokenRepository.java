package com.example.JWTSecurity.Repository;

import com.example.JWTSecurity.Model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer>{

    Optional<Token> findTokenByJwt(String jwt);
    @Query("""
            SELECT t from Token t where t.user.id = :id and t.isRevoked = false 
            """)
    @Transactional(readOnly = true)
    List<Token> findTokensByUserIdNotRevoked(Integer id);
}
