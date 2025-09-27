package com.example.demo.DTO.RequestDTO;
import com.example.demo.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserLoginDTO {
    String email;
    String password;
}
