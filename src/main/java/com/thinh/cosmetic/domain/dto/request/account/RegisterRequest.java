package com.thinh.cosmetic.domain.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class RegisterRequest {
    @NotBlank private String fullName;
    @NotBlank @Email private String email;
    private String phone;
    @NotBlank @Size(min = 6) private String password;
}
