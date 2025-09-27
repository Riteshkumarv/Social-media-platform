package com.example.demo.controller;

import com.example.demo.DTO.RequestDTO.UserRequestDTO;
import com.example.demo.DTO.RequestDTO.UserUpdateDTO;
import com.example.demo.DTO.ResponseDTO.UserResponseDTO;
import com.example.demo.entites.PasswordResetToken;
import com.example.demo.entites.Users;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @PostMapping("/user/add")
    public UserResponseDTO addUser(@RequestBody UserRequestDTO userRequestDTO){
        return userService.addUser(userRequestDTO);
    }

    @GetMapping("/user/all")
    public List<UserResponseDTO> getUser(){
        return userService.getUser();
    }

    @GetMapping("/user/get")
    public UserResponseDTO getUserById(@RequestParam String email) {
        return userService.getUserById(email);
    }

    @GetMapping("/user/notification/get")
    public UserResponseDTO getTheUserWhichIsReceviedMaxNumberOfNotification(){
        return userService.getTheUserWhichIsReceviedMaxNumberOfNotification();
    }

    @GetMapping("/user/get/all")
    public Page<UserResponseDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }


//    @GetMapping("/api/user/me")
//    public UserResponseDTO getCurrentUser(@AuthenticationPrincipal Users user) {
//        if (user == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
//        }
//
//        return new UserResponseDTO(
//                user.getUserId(),
//                user.getUsername(),
//                user.getAge()
//        );
//    }

    @GetMapping("/api/user/me")
    public ResponseEntity<UserResponseDTO> getUserDetails(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Assuming you have a service to fetch your Users entity by username/email
        Users user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Map Users entity to UserResponseDTO (exclude lazy collections like posts)
        UserResponseDTO dto = new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getAge(),
                user.getEmail(),
                user.getFollowers().size(),
                user.getFollowing().size(),
                user.getProfilePictureUrl()
                // add other fields you want to expose
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

        if(email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        Users user = userService.findByEmail(email);

        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);

        PasswordResetToken otpToken = new PasswordResetToken();
        otpToken.setUser(user);
        otpToken.setToken(passwordEncoder.encode(String.valueOf(otp)));
        otpToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        passwordResetTokenRepository.save(otpToken);

        emailService.sendOtpEmail(email , String.valueOf(otp));

        return ResponseEntity.ok("OTP sent to " + email);
    }

    @GetMapping("/getId-by-email")
    public ResponseEntity<Integer> getIdByEmail(@RequestParam String email) {
        if(email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        Users user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user.getUserId());
    }

    @PutMapping("/user/update")
    public ResponseEntity<UserUpdateDTO> updateUser(
            @RequestParam String email,
            @RequestBody UserUpdateDTO userUpdateDTO) {
        UserUpdateDTO userUpdate = userService.updateUser(email, userUpdateDTO);
        return ResponseEntity.ok(userUpdate);
    }

    @GetMapping("/api/users/search")
    public List<UserResponseDTO> searchUsers(@RequestParam String name) {
        List<Users> users = userService.findByNameContainingIgnoreCase(name);
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found with the given name");
        }
        List<UserResponseDTO> dtoList = userService.convertToUserResponseDTOList(users);
        return dtoList;
    }

    @GetMapping("/api/users/get-by-id")
    public ResponseEntity<UserResponseDTO> getUserByUserId(@RequestParam int userId) {
        UserResponseDTO user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }
}
