package com.example.demo.controller;

import com.example.demo.DTO.PostDTO;
import com.example.demo.DTO.RequestDTO.PostRequestDTO;
import com.example.demo.entites.Comment;
import com.example.demo.entites.Posts;
import com.example.demo.entites.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepo;

    @PostMapping("/user/post/{id}")
    public PostDTO addPost(@PathVariable int id , @RequestBody PostRequestDTO posts){
        return postService.addPost(id , posts);
    }

    @GetMapping("/user/post")
    public List<PostDTO> getAllPosts(){
        return postService.getAllPosts();
    }

    @PostMapping("/create")
    public ResponseEntity<Posts> createPost(
            Authentication authentication,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        // Get logged-in user's email/username from Authentication
        String email = authentication.getName();

        // Fetch the actual Users entity from DB
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                postService.createPost(user.getUserId(), content, image)
        );
    }



    @PostMapping("/{postId}/like")
    public ResponseEntity<Posts> likePost(@PathVariable int postId) {
        return ResponseEntity.ok(postService.likePost(postId));
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<Posts> addComment(
            @PathVariable int postId,
            @RequestParam("text") String text
    ) {
        Comment comment = new Comment();
        comment.setText(text);
        return ResponseEntity.ok(postService.addComment(postId, comment));
    }

    // Controller
    @PostMapping("/api/pictures/upload")
    public ResponseEntity<String> uploadPicture(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        String email = authentication.getName();
        System.out.println(email);
        String imageUrl = postService.uploadPicture(file, email);
        return ResponseEntity.ok(imageUrl); // Return the public URL
    }

    @GetMapping("/api/pictures")
    public ResponseEntity<List<String>> getPictures() {
        // Hardcoded list of picture URLs
        List<String> pictures = List.of(
                "https://res.cloudinary.com/dpdg2zia4/image/upload/v1754906665/user_uploads/Ritesh74461%40gmail.com/bz6fhwezykogsbdzvl4d.jpg",
                "https://res.cloudinary.com/dpdg2zia4/image/upload/v1754906665/user_uploads/Ritesh74461%40gmail.com/bz6fhwezykogsbdzvl4d.jpg"
        );
        return ResponseEntity.ok(pictures);
    }

    @GetMapping("/user/post/{userId}")
    public List<PostDTO> getPostsByUserId(@PathVariable int userId) {
        return postService.getPostsByUserId(userId);

    }

    @GetMapping("/posts/others")
    public List<PostDTO> getOtherUsersPosts(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal(); // logged-in user's email
        Users user = customUserDetails.getUser();
        String email = user.getEmail(); // Get the email of the logged-in user
        return postService.getPostsExceptUser(email);
    }
}
