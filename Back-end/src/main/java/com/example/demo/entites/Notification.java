package com.example.demo.entites;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Posts posts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users users;

    @OneToOne(mappedBy = "notification" , cascade = CascadeType.ALL)
    Likee likee;

    @OneToOne(mappedBy = "notification" , cascade = CascadeType.ALL)
    Comment comment;

    LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "action_by")
    Users actionBy;
}
