package com.example.demo.mapper;

import com.example.demo.DTO.PostDTO;
import com.example.demo.DTO.RequestDTO.PostRequestDTO;
import com.example.demo.entites.Posts;

import java.time.LocalDateTime;

public class PostMapper {

    public PostDTO postEntityConvertToPostDTO(Posts post){
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setContant(post.getContent());
        postDTO.setTime(post.getTime());
        postDTO.setImageUrl(post.getImageUrl());
        return postDTO;
    }

    public Posts postDTOConvertToPostEntity(PostDTO postDTO){
        Posts post = new Posts();
        post.setTime(postDTO.getTime());
        post.setContent(postDTO.getContant());
        post.setId(postDTO.getId());
        post.setImageUrl(postDTO.getImageUrl());
        return post;
    }


    public Posts postRequestDTOConvertToPostEntity(PostRequestDTO postDTO){
        return Posts.builder()
                .content(postDTO.getContent())
                .time(LocalDateTime.now())
                .imageUrl(postDTO.getImageUrl())
                .build();
    }
}
