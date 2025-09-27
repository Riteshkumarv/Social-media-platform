package com.example.demo.entites;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    LocalDateTime time;
    String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Posts posts;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id")
    Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users commentedBy;

    private String text;

}
