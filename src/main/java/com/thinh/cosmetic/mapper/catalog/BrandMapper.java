package com.thinh.cosmetic.mapper.catalog;

import com.thinh.cosmetic.domain.dto.request.catalog.BrandRequest;
import com.thinh.cosmetic.domain.dto.response.catalog.BrandResponse;
import com.thinh.cosmetic.domain.entity.catalog.BrandEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BrandMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    BrandEntity toEntity(BrandRequest request);

    BrandResponse toResponse(BrandEntity entity);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    void updateEntity(BrandRequest request, @MappingTarget BrandEntity entity);
}
