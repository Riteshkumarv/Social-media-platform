package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.DTO.PostDTO;
import com.example.demo.DTO.RequestDTO.PostRequestDTO;
import com.example.demo.controller.UserController;
import com.example.demo.entites.Notification;
import com.example.demo.entites.Posts;
import com.example.demo.entites.Users;
import com.example.demo.mapper.PostMapper;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.NotificationFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    PostRepository postRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    NotificationRepository notificationRepo;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    PostMapper postMapper = new PostMapper();

    public PostDTO addPost(int id , PostRequestDTO posts){
        Posts postss = Posts.builder()
                .users(userRepo.findById(id).get())
                .content(posts.getContent())
                .imageUrl(posts.getImageUrl())
                .time(LocalDateTime.now())
                .build();
        Posts savedPost = postRepo.save(postss);
        return postMapper.postEntityConvertToPostDTO(savedPost);
    }

    public List<PostDTO> getAllPosts() {
        List<Posts> post = postRepo.findAll();
        List<PostDTO> postDTO = new ArrayList<>();

        for(Posts posts : post){
            postDTO.add(postMapper.postEntityConvertToPostDTO(posts));
        }

        return postDTO;
    }

    public Posts createPost(int userId, String content, MultipartFile image) throws IOException {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl;
        if (image != null && !image.isEmpty()) {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            image.transferTo(new File(UPLOAD_DIR + fileName));
            imageUrl = "/uploads/" + fileName;
        } else {
            imageUrl = "https://via.placeholder.com/150?text=Default+Pic";
        }

        Posts post = Posts.builder()
                .content(content)
                .imageUrl(imageUrl)
                .time(LocalDateTime.now())
                .users(user)
                .build();

        return postRepo.save(post);
    }

    public Posts likePost(int postId) {
        Posts post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.getLikee().add(new com.example.demo.entites.Likee(null, post)); // assuming Likee has constructor
        return postRepo.save(post);
    }

    public Posts addComment(int postId, com.example.demo.entites.Comment comment) {
        Posts post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setPosts(post);
        post.getComment().add(comment);
        return postRepo.save(post);
    }

    @Autowired
    private Cloudinary cloudinary;

    public String uploadPicture(MultipartFile file, String email) {
        try {
            Users user = userRepo.findByEmail(email).get();
            user.setProfilePictureUrl(file.toString());

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "user_uploads/" + email));
            String imageUrl = uploadResult.get("secure_url").toString();
            user.setProfilePictureUrl(imageUrl);
            userRepo.save(user);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload picture", e);
        }
    }

    public String getPictureUrl(int postId) {
        Posts post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getImageUrl();
    }

    public List<PostDTO> getPostsByUserId(int userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Posts> posts = postRepo.findByUsers(user);
        List<PostDTO> postDTOs = new ArrayList<>();

        for (Posts post : posts) {
            postDTOs.add(postMapper.postEntityConvertToPostDTO(post));
        }

        return postDTOs;
    }

    public List<PostDTO> getPostsExceptUser(String email) {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Posts> posts = postRepo.findByUsersNotOrderByTimeDesc(user);

        List<PostDTO> postDTOS = new ArrayList<>();

        for (Posts post : posts) {
            PostDTO postDTO = postMapper.postEntityConvertToPostDTO(post);
            postDTOS.add(postDTO);
        }
        return postDTOS;
    }
}
