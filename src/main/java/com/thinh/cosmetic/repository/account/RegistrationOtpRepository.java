package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.RegistrationOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationOtpRepository extends JpaRepository<RegistrationOtpEntity, Long> {

    Optional<RegistrationOtpEntity> findTopByEmailIgnoreCaseAndUsedFalseOrderByCreatedAtDesc(String email);

    Optional<RegistrationOtpEntity> findTopByEmailIgnoreCaseOrderByCreatedAtDesc(String email);

    List<RegistrationOtpEntity> findByEmailIgnoreCaseAndUsedFalse(String email);
}
