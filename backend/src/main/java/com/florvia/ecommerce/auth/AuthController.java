package com.florvia.ecommerce.auth;

import com.florvia.ecommerce.auth.dto.LoginRequest;
import com.florvia.ecommerce.auth.dto.LoginResponse;
import com.florvia.ecommerce.auth.dto.RegisterRequest;
import com.florvia.ecommerce.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Void> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);
        return ApiResponse.success("Register success", null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ApiResponse.success(
                "Login success",
                authService.login(request)
        );
    }
}
