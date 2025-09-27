package com.example.demo.service;

import com.example.demo.DTO.RequestDTO.UserRequestDTO;
import com.example.demo.DTO.RequestDTO.UserUpdateDTO;
import com.example.demo.DTO.ResponseDTO.UserResponseDTO;
import com.example.demo.entites.Users;
import com.example.demo.mapper.PostMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    PostMapper postMapper = new PostMapper();

    UserMapper userMapper = new UserMapper();

    public UserResponseDTO addUser(UserRequestDTO userRequestDTO){
        Users user = userMapper.UserRequestDTOConvertToUserEntity(userRequestDTO);

        Users saveUser = userRepo.save(user);
        return userMapper.UserEntityConvertToUserResponseDTO(saveUser);
    }

    public List<UserResponseDTO> getUser() {
        List<Users> users = userRepo.findAll();
        List<UserResponseDTO> userResponseDTO = new ArrayList<>();

        for(Users user : users){
            UserResponseDTO tempDTO = new UserResponseDTO();
            tempDTO = userMapper.UserEntityConvertToUserResponseDTO(user);
            userResponseDTO.add(tempDTO);
        }

        return userResponseDTO;
    }

    public Page<UserResponseDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepo.findAll(pageable)
                .map(userMapper::UserEntityConvertToUserResponseDTO);
    }


    public UserResponseDTO getTheUserWhichIsReceviedMaxNumberOfNotification() {
        List<Users> users = userRepo.findAll();

        int max = 0;
        int userId = 0;

        for(Users user : users){
            if(user.getNotifications().size() > max){
                max = user.getNotifications().size();
                userId = user.getUserId();
            }
        }
        return userMapper.UserEntityConvertToUserResponseDTO(userRepo.findById(userId).get());
    }


    public Users findByUsername(String username) {
        return (Users) userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public Users findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public void saveUser(Users user) {
        if (user != null) {
            userRepo.save(user);
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    public UserResponseDTO getUserById(String email) {
        return userMapper.UserEntityConvertToUserResponseDTO(
                userRepo.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + email))
        );
    }

    public UserUpdateDTO updateUser(String email, UserUpdateDTO userRequestDTO) {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        user.setName(userRequestDTO.getName());
        user.setAge(userRequestDTO.getAge());
        user.setProfilePictureUrl(userRequestDTO.getProfilePictureUrl());
        Users updatedUser = userRepo.save(user);
        return userMapper.UserEntityConvertToUserUpdateDTO(updatedUser);
    }

    public void followUser(int userId, int targetUserId) {
        Users curretUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Users targetUser = userRepo.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found with ID: " + targetUserId));

        curretUser.getFollowing().add(targetUser);
        targetUser.getFollowers().add(curretUser);
        userRepo.save(curretUser);
        userRepo.save(targetUser);
    }

    public void unfollowUser(int userId, int targetUserId) {
        Users currentUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Users targetUser = userRepo.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found with ID: " + targetUserId));

        currentUser.getFollowing().remove(targetUser);
        targetUser.getFollowers().remove(currentUser);
        userRepo.save(currentUser);
        userRepo.save(targetUser);
    }

    public List<Users> findByNameContainingIgnoreCase(String name) {
         return userRepo.findByNameContainingIgnoreCase(name);
    }

    public List<UserResponseDTO> convertToUserResponseDTOList(List<Users> users) {
    List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        for (Users user : users) {
            UserResponseDTO dto = userMapper.UserEntityConvertToUserResponseDTO(user);
            userResponseDTOList.add(dto);
        }
        return userResponseDTOList;
    }

    public UserResponseDTO getUserByUserId(int userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return userMapper.UserEntityConvertToUserResponseDTO(user);
    }
}
