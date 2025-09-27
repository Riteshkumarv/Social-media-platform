package com.example.demo.DTO.RequestDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateDTO {
    String name;
    int age;
    String profilePictureUrl;
}

