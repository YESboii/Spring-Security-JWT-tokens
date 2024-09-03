package com.example.JWTSecurity.controllers.utils;

import com.example.JWTSecurity.Model.User;



public record RegistrationResponse(User user,String message) {
}
