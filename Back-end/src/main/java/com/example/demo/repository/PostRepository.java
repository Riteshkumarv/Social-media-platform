package com.example.demo.repository;

import com.example.demo.entites.Posts;
import com.example.demo.entites.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {
    List<Posts> findAllByOrderByTimeDesc();

    List<Posts> findByUsers(Users user);

    List<Posts> findByUsersNot(Users user);

    List<Posts> findByUsersNotOrderByTimeAsc(Users user);

    List<Posts> findByUsersNotOrderByTimeDesc(Users user);
}
