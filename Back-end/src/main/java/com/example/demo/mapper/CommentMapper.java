package com.example.demo.mapper;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.ResponseDTO.CommentResponseDTO;
import com.example.demo.entites.Comment;

public class CommentMapper {

    public CommentDTO commentEntityConvertToCommentDTO (Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment(comment.getComment());
        commentDTO.setTime(comment.getTime());
        return commentDTO;
    }

    public Comment commentDTOConvertToCommentEntity(CommentDTO commentDTO){
        return Comment.builder()
                .comment(commentDTO.getComment())
                .time(commentDTO.getTime())
                .build();
    }

    public CommentResponseDTO commentEntityConvertToCommentResponseDTO(Comment comment){
        return CommentResponseDTO.builder()
                .commentedBy(comment.getCommentedBy().getName())
                .id(comment.getId())
                .notificationId(comment.getNotification().getId())
                .postId(comment.getPosts().getId())
                .comment(comment.getComment())
                .time(comment.getTime())
                .build();
    }

}
