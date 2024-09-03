package com.example.JWTSecurity.controllers.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(@JsonProperty("access_token") String accessToken,@JsonProperty("refresh_token") String RefreshToken) {
}
