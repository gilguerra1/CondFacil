package com.condofacil.dto;

import com.condofacil.model.RoleType;
import jakarta.validation.constraints.*;

public record UserRegistrationDTO(
        @NotBlank String fullName,
        @NotBlank @Pattern(regexp = "\\d{11}") String taxId,
        @Email String email,
        String phone,
        @NotBlank String login,
        @NotBlank @Size(min = 6) String password,
        @NotNull RoleType roleType
        ) {
}
