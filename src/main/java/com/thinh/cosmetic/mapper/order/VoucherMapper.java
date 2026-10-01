package com.thinh.cosmetic.mapper.order;

import com.thinh.cosmetic.domain.dto.request.order.VoucherRequest;
import com.thinh.cosmetic.domain.dto.response.order.VoucherResponse;
import com.thinh.cosmetic.domain.entity.order.VoucherEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VoucherMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usedQuantity", ignore = true)
    VoucherEntity toEntity(VoucherRequest request);

    VoucherResponse toResponse(VoucherEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usedQuantity", ignore = true)
    void updateEntity(VoucherRequest request, @MappingTarget VoucherEntity entity);
}
