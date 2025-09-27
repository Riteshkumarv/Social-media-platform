package com.example.demo.controller;

import com.example.demo.DTO.RequestDTO.UserRequestDTO;
import com.example.demo.entites.Users;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.GoogleTokenVerifier;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    UserMapper userMapper = new UserMapper();

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register API
     */
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody UserRequestDTO userDto) {
        // Check if email already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Map DTO to Entity
        Users newUser = userMapper.UserRequestDTOConvertToUserEntity(userDto);

        // Encode password
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Save to DB
        userRepository.save(newUser);

        // Generate JWT token
        String token = jwtUtil.generateToken(newUser.getEmail() , newUser.getRole());

        return buildResponse(token, "User registered successfully");
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        // 1. Validate refresh token
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired refresh token"));
        }

        // 2. Extract email from refresh token
        String email = jwtUtil.extractUsernameFromRefresh(refreshToken);

        // 3. Fetch user
        Users existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Generate new access token
        String newAccessToken = jwtUtil.generateToken(existingUser.getEmail(), existingUser.getRole());

        // 5. Return response
        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "message", "New access token generated successfully"
        ));
    }



    /**
     * Login API
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> userDto) {
        // 1. Find user by email
        Users existingUser = userRepository.findByEmail(userDto.get("email"))
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. Validate password
        if (!passwordEncoder.matches(userDto.get("password"), existingUser.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Generate tokens
        String accessToken = jwtUtil.generateToken(existingUser.getEmail(), existingUser.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(existingUser.getEmail(), existingUser.getRole());

        // 4. Return response
        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "message", "Login successful"
        ));
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        try {
            String idToken = body.get("idToken");
            if (idToken == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Missing idToken"));
            }

            // 1. Verify Google token and get email
            String email = GoogleTokenVerifier.verifyToken(idToken);



            // 2. Check if user exists
            Users existingUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Email not registered. Please use normal login."));

            // 3. Generate JWT tokens
            String accessToken = jwtUtil.generateToken(existingUser.getEmail(), existingUser.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(existingUser.getEmail(), existingUser.getRole());

            // 4. Return tokens
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "message", "Google login successful"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }



    @GetMapping("/whoami")
    public Map<String, String> whoami(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String role = jwtUtil.extractRole(jwt);
        String email = jwtUtil.extractUsername(jwt);
        return Map.of("email", email, "role", role);
    }



    private Map<String, String> buildResponse(String token, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("token", token);
        return response;
    }
}
