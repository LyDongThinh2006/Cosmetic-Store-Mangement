package com.thinh.cosmetic.domain.dto.request.account;

import com.thinh.cosmetic.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CustomerProfileRequest {
    private String fullName;
    private String phone;
    private LocalDate dob;
    private Gender gender;
}
