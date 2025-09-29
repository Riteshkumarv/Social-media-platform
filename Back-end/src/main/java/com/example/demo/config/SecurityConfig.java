package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Global CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // ----- API CORS (frontend calls backend) -----
        CorsConfiguration apiCors = new CorsConfiguration();
        apiCors.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",                    // local React
                "https://frontend-production-0e87.up.railway.app" // prod frontend
        ));
        apiCors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        apiCors.setAllowedHeaders(Arrays.asList("*"));
        apiCors.setAllowCredentials(true); // cookies / JWT allowed

        // ----- Swagger CORS (Swagger calls backend) -----
        CorsConfiguration swaggerCors = new CorsConfiguration();
        swaggerCors.setAllowedOriginPatterns(Arrays.asList("*")); // allow all origins
        swaggerCors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        swaggerCors.setAllowedHeaders(Arrays.asList("*"));
        swaggerCors.setAllowCredentials(false); // no JWT / cookies needed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", apiCors);           // API endpoints
        source.registerCorsConfiguration("/swagger-ui/**", swaggerCors); // Swagger UI
        source.registerCorsConfiguration("/v3/api-docs/**", swaggerCors); // Swagger docs

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Disable CSRF
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        // Preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Public endpoints
                        .requestMatchers(
                                "/auth/**",
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**",
                                "/forgot-password/**",
                                "/enter-OTP/**",
                                "/enter-new-password/**"
                        ).permitAll()
                        // Role-based endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        // Secured APIs
                        .requestMatchers(HttpMethod.GET, "/api/pictures/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/pictures/**").authenticated()
                        // All other requests need authentication
                        .anyRequest().authenticated()
                )
                // Stateless sessions
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
