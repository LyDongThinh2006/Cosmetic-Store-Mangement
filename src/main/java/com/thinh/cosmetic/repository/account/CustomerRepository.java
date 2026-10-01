package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
