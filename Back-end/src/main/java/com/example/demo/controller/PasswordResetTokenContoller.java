package com.example.demo.controller;

import com.example.demo.entites.PasswordResetToken;
import com.example.demo.entites.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class PasswordResetTokenContoller {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/enter-OTP")
    public String enterOTP(@RequestParam String email , @RequestParam String otp) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        List<PasswordResetToken> list = user.getPasswordResetTokens();
        PasswordResetToken passwordResetToken = list.get(list.size() - 1);

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if(!passwordEncoder.matches(otp , passwordResetToken.getToken())) {
            throw new RuntimeException("Invalid OTP");
        }

        return "OTP entered successfully";
    }

    @PostMapping("/enter-new-password")
    public ResponseEntity<String> enterNewPassword(@RequestParam String email, @RequestParam String password) {

        if (password == null || password.isBlank()) {
            throw new RuntimeException("Password cannot be null or empty");
        }

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

}
