package com.example.demo.security;

import com.example.demo.entites.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Users user;

    public CustomUserDetails(Users user) {
        this.user = user;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming user.getRole() returns an enum or string like ADMIN / USER
        return Collections.singletonList(() -> "ROLE_" + user.getRole().name());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Users getUser() {
        return user;
    }
}
