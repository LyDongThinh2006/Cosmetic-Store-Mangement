package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.PasswordResetOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtpEntity, Long> {

    Optional<PasswordResetOtpEntity> findTopByAccountAndUsedFalseOrderByCreatedAtDesc(AccountEntity account);

    Optional<PasswordResetOtpEntity> findTopByIdentifierIgnoreCaseAndUsedFalseOrderByCreatedAtDesc(String identifier);

    Optional<PasswordResetOtpEntity> findTopByAccountOrderByCreatedAtDesc(AccountEntity account);

    Optional<PasswordResetOtpEntity> findTopByIdentifierIgnoreCaseOrderByCreatedAtDesc(String identifier);

    List<PasswordResetOtpEntity> findByAccountAndUsedFalse(AccountEntity account);
}
