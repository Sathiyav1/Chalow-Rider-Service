package com.chalow.rideshare.service;

import com.chalow.rideshare.dto.UserDTO;
import com.chalow.rideshare.entity.User;
import com.chalow.rideshare.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getCurrentUserOrCreate(Authentication authentication) {
        String username = authentication.getName();
        var user = userRepository.findByUsername(username).orElseGet(() -> {
            var u = new User();
            u.setUsername(username);
            u.setDisplayName(username);
            return userRepository.save(u);
        });
        return new UserDTO(user.getId(), user.getUsername());
    }

    public boolean canUserRequestRide(UserDTO user) {
        return true;
    }

    public boolean canUserRequestRide(User user) {
        return true;
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
    }
}
