package com.example.demo.mapper;

import com.example.demo.DTO.NotificationCommentDTO;
import com.example.demo.DTO.NotificationLikeDTO;
import com.example.demo.entites.Notification;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationMapper {

    @Autowired
    UserRepository userRepository;

    public NotificationCommentDTO notificationEntityConvertToNotificationCommentDTO(Notification notification){
        NotificationCommentDTO notificationCommentDTO = new NotificationCommentDTO();
        notificationCommentDTO.setComment(notification.getComment().getComment());
        notificationCommentDTO.setDate(notification.getDate());
        notificationCommentDTO.setContant(notification.getPosts().getContent());
        notificationCommentDTO.setUserName(notification.getUsers().getName());
        notificationCommentDTO.setCommentBy((notification.getActionBy().getName()));
        return notificationCommentDTO;
    }

    public NotificationLikeDTO notificationEntityConvertToNotificationLikeDTO(Notification notification){
        NotificationLikeDTO notificationLikeDTO = new NotificationLikeDTO();
        notificationLikeDTO.setLike("Your pic is liked");
        notificationLikeDTO.setContant(notification.getPosts().getContent());
        notificationLikeDTO.setDate(notification.getDate());
        notificationLikeDTO.setUserName(notification.getUsers().getName());
        notificationLikeDTO.setLikedBy(notification.getActionBy().getName());
        return notificationLikeDTO;
    }
}
