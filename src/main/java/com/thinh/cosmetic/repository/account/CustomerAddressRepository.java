package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.CustomerAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddressEntity, Long> {
    List<CustomerAddressEntity> findByCustomerEntityId(Long customerId);
}
