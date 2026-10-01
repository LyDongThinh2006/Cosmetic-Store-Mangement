package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
