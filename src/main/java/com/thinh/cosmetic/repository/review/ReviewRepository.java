package com.thinh.cosmetic.repository.review;

import com.thinh.cosmetic.domain.entity.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
