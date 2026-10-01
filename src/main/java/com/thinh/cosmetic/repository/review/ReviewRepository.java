package com.thinh.cosmetic.repository.review;

import com.thinh.cosmetic.domain.entity.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByProductId(Long productId);
    List<ReviewEntity> findByCustomerId(Long customerId);
}
