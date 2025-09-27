package com.example.demo.service;

import com.example.demo.DTO.NotificationLikeDTO;
import com.example.demo.DTO.ResponseDTO.UserResponseDTO;
import com.example.demo.entites.Notification;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepo;

    NotificationMapper notificationMapper = new NotificationMapper();

    UserMapper userMapper = new UserMapper();

    public List<NotificationLikeDTO> getAllNotificationByUserId(int userId) {
        List<Notification> notifications = userRepo.findById(userId).get().getNotifications();
        List<NotificationLikeDTO> notificationLikeDTOS = new ArrayList<>();
        for(Notification notification : notifications){
            notificationLikeDTOS.add(notificationMapper.notificationEntityConvertToNotificationLikeDTO(notification));
        }
        return notificationLikeDTOS;
    }
}
