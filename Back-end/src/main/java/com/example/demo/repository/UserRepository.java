package com.example.demo.repository;

import com.example.demo.entites.Users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
    Page<Users> findAll(Pageable pageable);



    List<Users> findByNameContainingIgnoreCase(String name);

}
