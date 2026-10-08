package com.thinh.cosmetic.domain.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRequest {
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 150, message = "Full name must not exceed 150 characters")
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email format is invalid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Email(message = "Internal email format is invalid")
    @Size(max = 150, message = "Internal email must not exceed 150 characters")
    private String internalEmail;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    private List<Long> roleIds;

    private List<Long> storeIds;
}
