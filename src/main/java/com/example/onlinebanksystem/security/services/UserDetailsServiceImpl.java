package com.example.onlinebanksystem.security.services;

import com.example.onlinebanksystem.model.entity.User;
import com.example.onlinebanksystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByFin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with FIN: " + username));

        return UserDetailsImpl.build(user);
    }

}