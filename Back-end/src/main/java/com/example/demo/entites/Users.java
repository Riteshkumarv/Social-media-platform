package com.example.demo.entites;

import com.example.demo.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String name;
    private int age;

    String email;
    String password;

    @Enumerated(EnumType.STRING)
    Role role;

    @OneToMany(mappedBy = "users" , cascade = CascadeType.ALL)
    private List<Posts> posts = new ArrayList<>();

    @OneToMany(mappedBy = "users" , cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "commentedBy", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "likedBy", cascade = CascadeType.ALL)
    private List<Likee> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + role.name());
    }

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"), // current user
            inverseJoinColumns = @JoinColumn(name = "follower_id") // follower
    )
    private List<Users> followers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "user_id"), // current user
            inverseJoinColumns = @JoinColumn(name = "following_id") // following
    )
    private List<Users> following = new ArrayList<>();

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public String getPassword() {
        return password;
    }

    String profilePictureUrl;
}
