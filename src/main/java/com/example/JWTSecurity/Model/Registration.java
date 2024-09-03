package com.example.JWTSecurity.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {@Index(name = "key_index",columnList = "registrationKey")})
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(unique = true)
    private String registrationKey;

    private LocalDateTime expiration;


}
