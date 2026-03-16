package com.florvia.ecommerce.user;

import com.florvia.ecommerce.entity.User;
import com.florvia.ecommerce.exception.ResourceNotFoundException;
import com.florvia.ecommerce.repository.UserRepository;
import com.florvia.ecommerce.security.CustomUserDetails;
import com.florvia.ecommerce.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
