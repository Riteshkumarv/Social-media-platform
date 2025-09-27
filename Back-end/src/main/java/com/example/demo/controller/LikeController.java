package com.example.demo.controller;

import com.example.demo.DTO.NotificationLikeDTO;
import com.example.demo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LikeController {

    @Autowired
    LikeService likeService;

    @PostMapping("/user/like/{userId}")
    public NotificationLikeDTO likeAPost(@PathVariable int userId, @RequestParam int postId){
        return likeService.likeAPost(userId , postId);
    }

}
