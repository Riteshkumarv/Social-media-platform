package com.example.demo.service;


import com.example.demo.DTO.NotificationCommentDTO;
import com.example.demo.DTO.ResponseDTO.CommentResponseDTO;
import com.example.demo.entites.*;

import com.example.demo.mapper.CommentMapper;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    NotificationMapper notificationMapper = new NotificationMapper();

    CommentMapper commentMapper = new CommentMapper();

//    public CommentDTO addComment(int id, Comment comment) {
//
//        Posts post = postRepository.findById(id).get();
//        comment.setPosts(post);
//        commentRepository.save(comment);
//        return commentMapper.commentEntityConvertToCommentDTO(comment);
//    }

    public NotificationCommentDTO addComment(int userId, int postId, String comment) {

        Users commentUser = userRepository.findById(userId).get();

        Posts post = postRepository.findById(postId).get();

        Comment commentObj = new Comment();

        commentObj.setComment(comment);
        commentObj.setTime(LocalDateTime.now());
        commentObj.setPosts(post);
        commentObj.setCommentedBy(commentUser);

        Notification notification = new Notification();
        notification.setComment(commentObj);
        notification.setDate(LocalDateTime.now());
        notification.setPosts(post);
        notification.setActionBy(commentUser);


        Users postOwner = userRepository.findById(post.getUsers().getUserId()).get();
        notification.setUsers(postOwner);
        List<Notification> notifications = postOwner.getNotifications();
        notifications.add(notification);
        postOwner.setNotifications(notifications);
        List<Comment> commentList = post.getComment();
        commentList.add(commentObj);
        post.setComment(commentList);
        commentObj.setNotification(notification);

        userRepository.save(postOwner);
        commentRepository.save(commentObj);
        postRepository.save(post);
        notificationRepository.save(notification);

        return notificationMapper.notificationEntityConvertToNotificationCommentDTO(notification);
    }

    public List<CommentResponseDTO> getAllCommentByUserId(int userId) {
        Users user = userRepository.findById(userId).get();
        List<Comment> comments = user.getComments();
        List<CommentResponseDTO> commentResponseDTOS = new ArrayList<>();
        for(Comment comment : comments){
            commentResponseDTOS.add(commentMapper.commentEntityConvertToCommentResponseDTO(comment));
        }
        return commentResponseDTOS;
    }

    public List<CommentResponseDTO> getAllCommentByPostId(int postId) {
        return postRepository.findById(postId)
                .map(Posts::getComment)  // Get comments from the post
                .orElseThrow(() -> new RuntimeException("Post not found")) // Handle missing post
                .stream()
                .map(commentMapper::commentEntityConvertToCommentResponseDTO) // Convert each Comment to DTO
                .toList(); // Collect to List (Java 16+) or use Collectors.toList() for older versions
    }
}
