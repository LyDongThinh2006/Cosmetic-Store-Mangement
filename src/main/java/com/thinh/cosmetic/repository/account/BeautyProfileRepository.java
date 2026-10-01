package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.BeautyProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeautyProfileRepository extends JpaRepository<BeautyProfileEntity, Long> {
}
