package com.thinh.cosmetic.mapper.purchase;

import com.thinh.cosmetic.domain.dto.request.purchase.SupplierRequest;
import com.thinh.cosmetic.domain.dto.response.purchase.SupplierResponse;
import com.thinh.cosmetic.domain.entity.purchase.SupplierEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {
    @Mapping(target = "id", ignore = true)
    SupplierEntity toEntity(SupplierRequest request);

    SupplierResponse toResponse(SupplierEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(SupplierRequest request, @MappingTarget SupplierEntity entity);
}
