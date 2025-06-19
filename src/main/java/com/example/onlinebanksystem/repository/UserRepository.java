package com.example.onlinebanksystem.repository;

import com.example.onlinebanksystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
