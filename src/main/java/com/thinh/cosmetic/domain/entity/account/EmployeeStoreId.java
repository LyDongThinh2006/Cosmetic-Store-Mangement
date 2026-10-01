package com.thinh.cosmetic.domain.entity.account;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeStoreId implements Serializable {
    private Long employee;
    private Long store;
}
