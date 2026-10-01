package com.thinh.cosmetic.domain.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class EmployeeRequest {
    @NotBlank private String fullName;
    @NotBlank @Email private String email;
    @NotBlank private String password;
    private String internalEmail;
    private String phone;
    private List<Long> roleIds;
    private List<Long> storeIds;
}
