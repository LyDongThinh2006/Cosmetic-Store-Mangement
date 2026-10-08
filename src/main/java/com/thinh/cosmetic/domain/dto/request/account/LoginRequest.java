package com.thinh.cosmetic.domain.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    private String identifier;

    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    public String getLoginIdentifier() {
        if (identifier != null && !identifier.isBlank()) {
            return identifier.trim();
        }
        if (email != null && !email.isBlank()) {
            return email.trim();
        }
        return "";
    }
}
