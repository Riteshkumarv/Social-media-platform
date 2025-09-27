package com.example.demo.DTO.ResponseDTO;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentResponseDTO {

    int id;

    LocalDateTime time;

    String comment;

    int postId;

    int notificationId;

    String commentedBy;
}

