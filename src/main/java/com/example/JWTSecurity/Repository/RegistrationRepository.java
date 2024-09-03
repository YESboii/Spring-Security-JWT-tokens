package com.example.JWTSecurity.Repository;

import com.example.JWTSecurity.Model.Registration;
import com.example.JWTSecurity.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Integer>{

    Optional<Registration> findRegistrationByRegistrationKey(String registrationKey);
}
