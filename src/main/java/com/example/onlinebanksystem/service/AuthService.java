package com.example.onlinebanksystem.service;

import com.example.onlinebanksystem.exception.InvalidCredentialsException;
import com.example.onlinebanksystem.exception.UserAlreadyExistsException;
import com.example.onlinebanksystem.model.dto.LoginRequest;
import com.example.onlinebanksystem.model.dto.SignupRequest;
import com.example.onlinebanksystem.model.entity.User;
import com.example.onlinebanksystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SignupRequest signupRequest) {

        if (userRepository.existsByFin(signupRequest.getFin())) {
            throw new UserAlreadyExistsException("FIN", signupRequest.getFin());
        }

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username", signupRequest.getUsername());
        }

        if (signupRequest.getEmail() != null && !signupRequest.getEmail().trim().isEmpty() && userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email", signupRequest.getEmail());
        }

        if (signupRequest.getPhone() != null && !signupRequest.getPhone().trim().isEmpty() && userRepository.existsByPhone(signupRequest.getPhone())) {
            throw new UserAlreadyExistsException("Phone", signupRequest.getPhone());
        }

        User user = new User(
                signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getFin(),
                signupRequest.getEmail(),
                signupRequest.getPhone()
        );

        return userRepository.save(user);
    }

    public User authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByFin(loginRequest.getFin())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid FIN or password."));
        return user;
    }

}
