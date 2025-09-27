package com.example.demo.controller;

import com.example.demo.entites.Users;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class FollowController {

    @Autowired
    UserService userService;

    @PostMapping("/follow/{targetUserId}")
    public ResponseEntity<String> followUser(@PathVariable int targetUserId , Authentication auth){
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        Users curretUser = customUserDetails.getUser();
        userService.followUser(curretUser.getUserId(), targetUserId);
        return ResponseEntity.ok("Followed successfully");
    }

    @DeleteMapping("/unfollow/{targetUserId}")
    public ResponseEntity<String> unfollowUser(@PathVariable int targetUserId , Authentication auth){
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        Users curretUser = customUserDetails.getUser();
        userService.unfollowUser(curretUser.getUserId(), targetUserId);
        return ResponseEntity.ok("Unfollowed successfully");
    }
}
