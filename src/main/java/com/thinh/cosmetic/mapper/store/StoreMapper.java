package com.thinh.cosmetic.mapper.store;

import com.thinh.cosmetic.domain.dto.request.store.StoreRequest;
import com.thinh.cosmetic.domain.dto.response.store.StoreResponse;
import com.thinh.cosmetic.domain.entity.store.StoreEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreMapper {
    @Mapping(target = "id", ignore = true)
    StoreEntity toEntity(StoreRequest request);

    StoreResponse toResponse(StoreEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(StoreRequest request, @MappingTarget StoreEntity entity);
}
