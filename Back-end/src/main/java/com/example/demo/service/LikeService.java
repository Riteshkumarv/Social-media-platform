package com.example.demo.service;

import com.example.demo.DTO.NotificationLikeDTO;
import com.example.demo.entites.Likee;
import com.example.demo.entites.Notification;
import com.example.demo.entites.Posts;
import com.example.demo.entites.Users;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UserRepository userRepository;

    NotificationMapper notificationMapper = new NotificationMapper();

    public NotificationLikeDTO likeAPost(int userId, int postId) {

        Posts post = postRepository.findById(postId).get();

        Users likeUser = userRepository.findById(userId).get();

        postRepository.save(post);

        Notification notification = new Notification();

        notification.setPosts(post);
        notification.setUsers(post.getUsers());
        notification.setDate(LocalDateTime.now());
        notification.setActionBy(likeUser);

        Notification safedNoti = notificationRepository.save(notification);

        Users postOwner = post.getUsers();

        List<Notification> notifications = postOwner.getNotifications();
        notifications.add(safedNoti);
        postOwner.setNotifications(notifications);

        userRepository.save(postOwner);

        return notificationMapper.notificationEntityConvertToNotificationLikeDTO(notification);
    }
}
