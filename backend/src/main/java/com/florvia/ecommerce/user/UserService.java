package com.florvia.ecommerce.user;

import com.florvia.ecommerce.entity.User;
import com.florvia.ecommerce.exception.ResourceNotFoundException;
import com.florvia.ecommerce.repository.UserRepository;
import com.florvia.ecommerce.security.CustomUserDetails;
import com.florvia.ecommerce.user.dto.UpdateUserRequest;
import com.florvia.ecommerce.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Cacheable(value = "user_profile", key = "#email")
    public UserResponse getCurrentUser(CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }

    @CacheEvict(value = "user_profile", key = "#email")
    @Transactional
    public void updateProfile(String email, UpdateUserRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFullName(request.getFullName());
        userRepository.save(user);
    }
}
