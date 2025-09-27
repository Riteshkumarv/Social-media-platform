package com.example.demo.DTO.RequestDTO;
import com.example.demo.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Profile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRequestDTO {
    String name;
    int age;
    String email;
    String password;
    String profilePictureUrl;
}
