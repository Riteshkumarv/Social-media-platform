package com.example.demo.controller;

import com.example.demo.DTO.NotificationLikeDTO;
import com.example.demo.DTO.ResponseDTO.UserResponseDTO;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/user/notification/{userId}")
    public List<NotificationLikeDTO> getAllNotificationByUserId(@PathVariable int userId) {
        return notificationService.getAllNotificationByUserId(userId);
    }

}
