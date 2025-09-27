package com.example.demo.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content; // fixed typo "contant" -> "content"

    private String imageUrl; // for uploaded or default picture

    private LocalDateTime time;

    @PrePersist
    public void prePersist() {
        if (time == null) {
            time = LocalDateTime.now();
        }
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = "https://via.placeholder.com/150?text=Default+Pic";
        }
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // prevent infinite loop when sending JSON
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Likee> likee = new ArrayList<>();
}
