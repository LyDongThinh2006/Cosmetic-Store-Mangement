package com.thinh.cosmetic.mapper.catalog;

import com.thinh.cosmetic.domain.dto.request.catalog.CategoryRequest;
import com.thinh.cosmetic.domain.dto.response.catalog.CategoryResponse;
import com.thinh.cosmetic.domain.entity.catalog.CategoryEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity(CategoryRequest request);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    CategoryResponse toResponse(CategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(CategoryRequest request, @MappingTarget CategoryEntity entity);
}
