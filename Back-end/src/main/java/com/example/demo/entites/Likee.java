package com.example.demo.entites;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Likee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Posts posts;

    @OneToOne
    @JoinColumn(name = "notification_id")
    Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users likedBy;

    public Likee(Object o, Posts post) {
        likedBy = (Users) o;
        posts = post;
        time = LocalDateTime.now();
        notification = new Notification();
    }
}
