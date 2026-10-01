package com.thinh.cosmetic.mapper.account;

import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.email", target = "email")
    CustomerResponse toResponse(CustomerEntity entity);
}
