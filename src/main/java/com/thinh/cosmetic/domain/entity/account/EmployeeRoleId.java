package com.thinh.cosmetic.domain.entity.account;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRoleId implements Serializable {
    private Long employee;
    private Long role;
}
