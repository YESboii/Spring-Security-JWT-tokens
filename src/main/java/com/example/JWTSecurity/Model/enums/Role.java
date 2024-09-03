package com.example.JWTSecurity.Model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MANAGER,
    ;

    @Override
    public String getAuthority() {
        return toString();
    }
}
