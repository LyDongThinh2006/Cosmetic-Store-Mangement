package com.thinh.cosmetic.repository.returns;

import com.thinh.cosmetic.domain.entity.returns.ReturnItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReturnItemRepository extends JpaRepository<ReturnItemEntity, Long> {
    List<ReturnItemEntity> findByReturnRequestId(Long returnRequestId);
}
