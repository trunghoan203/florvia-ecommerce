package com.florvia.ecommerce.user.dto;

import com.florvia.ecommerce.entity.Role;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String email;
    private String fullName;
    private Role role;
}
