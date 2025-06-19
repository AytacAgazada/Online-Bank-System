package com.example.onlinebanksystem.repository;

import com.example.onlinebanksystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByFin(String fin);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    boolean existsByFin(String fin);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByFinOrUsernameOrEmailOrPhone(String fin, String username, String email, String phone);

    @Query("SELECT u FROM User u WHERE u.fin = :fin OR u.email = :email")
    Optional<User> findByFinOrEmail(@Param("fin") String fin, @Param("email") String email);

}
