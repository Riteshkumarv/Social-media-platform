package com.example.demo.repository;

import com.example.demo.entites.Likee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likee, Integer> {

}
