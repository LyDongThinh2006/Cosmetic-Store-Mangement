package com.thinh.cosmetic.domain.dto.response.account;

import com.thinh.cosmetic.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private Long accountId;
    private String email;
    private String phone;
    private String fullName;
    private LocalDate dob;
    private Gender gender;
    private Integer loyaltyPoints;
    private LocalDate joinDate;
}
