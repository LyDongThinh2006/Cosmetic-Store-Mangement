package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByAccountId(Long accountId);
}
