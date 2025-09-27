package com.example.demo.DTO.RequestDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PostRequestDTO {
    String content;
    String imageUrl;
}
