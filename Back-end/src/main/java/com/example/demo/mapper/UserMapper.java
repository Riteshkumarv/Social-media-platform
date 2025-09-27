package com.example.demo.mapper;


import com.example.demo.DTO.RequestDTO.UserLoginDTO;
import com.example.demo.DTO.RequestDTO.UserRequestDTO;
import com.example.demo.DTO.RequestDTO.UserUpdateDTO;
import com.example.demo.DTO.ResponseDTO.UserResponseDTO;
import com.example.demo.entites.Users;
import com.example.demo.enums.Role;
import org.apache.catalina.User;

public class UserMapper {

    public Users UserResponseDTOConvertToUserEntity(UserResponseDTO userResponseDTO){
       return Users.builder()
               .age(userResponseDTO.getAge())
               .name(userResponseDTO.getName())
               .userId(userResponseDTO.getId())
                .email(userResponseDTO.getEmail())
               .profilePictureUrl(userResponseDTO.getProfilePictureUrl())
               .build();
    }

    public UserResponseDTO UserEntityConvertToUserResponseDTO(Users user){
        return UserResponseDTO.builder()
        .age(user.getAge())
        .name(user.getName())
        .id(user.getUserId())
        .email(user.getEmail())
        .followersCount(user.getFollowers().size())
        .followingCount(user.getFollowing().size())
        .profilePictureUrl(user.getProfilePictureUrl())
        .build();
    }

    public Users UserRequestDTOConvertToUserEntity(UserRequestDTO userRequestDTO){
        return Users.builder()
                .age(userRequestDTO.getAge())
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .role(Role.USER)
                .password(userRequestDTO.getPassword())
                .profilePictureUrl(userRequestDTO.getProfilePictureUrl())
                .build();

    }

    public UserRequestDTO UserEntityConvertToUserRequestDTO(Users user){
        return UserRequestDTO.builder()
                .age(user.getAge())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    public UserUpdateDTO UserEntityConvertToUserUpdateDTO(Users user) {
        return UserUpdateDTO.builder()
                .age(user.getAge())
                .name(user.getName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }
    public Users UserUpdateDTOConvertToUserEntity(UserUpdateDTO userUpdateDTO) {
        return Users.builder()
                .age(userUpdateDTO.getAge())
                .name(userUpdateDTO.getName())
                .profilePictureUrl(userUpdateDTO.getProfilePictureUrl())
                .build();
    }
}
