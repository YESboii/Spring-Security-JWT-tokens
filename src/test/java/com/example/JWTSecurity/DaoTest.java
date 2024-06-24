package com.example.JWTSecurity;

import com.example.JWTSecurity.Model.User;
import com.example.JWTSecurity.Repository.UserRepository;
import com.example.JWTSecurity.Service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class DaoTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void test(){
        User user = userRepository.findByRegistrationKey("2cbcde49-624b-43ec-b097-7719db86592b").orElse(null);
        assertThat(user).isNotNull();
    }
}
