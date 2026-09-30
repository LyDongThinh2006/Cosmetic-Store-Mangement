package com.thinh.cosmetic.mapper.impl.catalog;

import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.mapper.Mapper;
import org.modelmapper.ModelMapper;

public class ProductMapperImpl implements Mapper<ProductEntity, Long> {
    private ModelMapper modelMapper;

    public ProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Long mapToDto(ProductEntity dto) {
        return null;
    }

    @Override
    public ProductEntity mapToEntity(Long entity) {
        return null;
    }
}
