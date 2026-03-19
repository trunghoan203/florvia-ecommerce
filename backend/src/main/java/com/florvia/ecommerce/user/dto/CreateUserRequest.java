package com.florvia.ecommerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 32, message = "Password must be between 6 and 32 characters")
    private String password;

    @NotBlank(message = "Full name must not be blank")
    private String fullName;
}
