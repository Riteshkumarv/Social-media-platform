package com.example.demo.entites;

import com.example.demo.entites.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private Users user;

    private LocalDateTime expiryDate;
}
