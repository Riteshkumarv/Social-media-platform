package com.example.demo.repository;

import com.example.demo.entites.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken , Integer> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser_UserId(Integer userId);

    void deleteByUser_UserId(Integer userId);
}


