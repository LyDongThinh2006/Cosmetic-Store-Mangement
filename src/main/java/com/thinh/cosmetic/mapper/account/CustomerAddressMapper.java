package com.thinh.cosmetic.mapper.account;

import com.thinh.cosmetic.domain.dto.request.account.CustomerAddressRequest;
import com.thinh.cosmetic.domain.dto.response.account.CustomerAddressResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerAddressEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerAddressMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerEntity", ignore = true)
    CustomerAddressEntity toEntity(CustomerAddressRequest request);

    CustomerAddressResponse toResponse(CustomerAddressEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerEntity", ignore = true)
    void updateEntity(CustomerAddressRequest request, @MappingTarget CustomerAddressEntity entity);
}
