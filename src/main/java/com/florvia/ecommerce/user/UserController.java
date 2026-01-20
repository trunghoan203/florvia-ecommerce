package com.florvia.ecommerce.user;

import com.florvia.ecommerce.common.ApiResponse;
import com.florvia.ecommerce.security.CustomUserDetails;
import com.florvia.ecommerce.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ApiResponse.success(
                "Get current user success",
                userService.getCurrentUser(userDetails)
        );
    }
}
