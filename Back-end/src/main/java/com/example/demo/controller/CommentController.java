package com.example.demo.controller;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.NotificationCommentDTO;
import com.example.demo.DTO.ResponseDTO.CommentResponseDTO;
import com.example.demo.entites.Comment;

import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/user/comment/add/{userId}")
    public NotificationCommentDTO addComment(@PathVariable int userId , @RequestParam int postId , @RequestBody String comment){
        return commentService.addComment(userId , postId , comment);
    }

    @GetMapping("/user/comment/getByUserId/{userId}")
    public List<CommentResponseDTO> getAllCommentByUserId(@PathVariable int userId){
        return commentService.getAllCommentByUserId(userId);
    }

    @GetMapping("/user/comment/getByPostId/{postId}")
    public List<CommentResponseDTO> getAllCommentByPostId(@PathVariable int postId){
        return commentService.getAllCommentByPostId(postId);
    }

}
