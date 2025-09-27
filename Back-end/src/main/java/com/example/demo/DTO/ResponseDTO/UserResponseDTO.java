package com.example.demo.DTO.ResponseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponseDTO {
    int id;
    String name;
    int age;
    String email;
    int followersCount;
    int followingCount;
    private String profilePictureUrl;
}
