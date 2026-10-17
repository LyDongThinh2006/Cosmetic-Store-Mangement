package com.thinh.cosmetic.service.catalog.impl;

import com.thinh.cosmetic.domain.dto.request.catalog.BrandRequest;
import com.thinh.cosmetic.domain.dto.response.catalog.BrandResponse;
import com.thinh.cosmetic.domain.entity.catalog.BrandEntity;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.mapper.catalog.BrandMapper;
import com.thinh.cosmetic.repository.catalog.BrandRepository;
import com.thinh.cosmetic.repository.catalog.ProductRepository;
import com.thinh.cosmetic.service.catalog.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final BrandMapper brandMapper;

    @Override
    public BrandResponse create(BrandRequest request) throws Exception {
        BrandEntity brand = brandMapper.toEntity(request);
        if (brand.getStatus() == null) {
            brand.setStatus(ActiveStatus.ACTIVE);
        }
        BrandEntity savedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(savedBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getById(Long id) throws Exception {
        BrandEntity brand = brandRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND, "Brand not found: " + id));

        return brandMapper.toResponse(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAll() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getActiveBrands() {
        return brandRepository.findByStatus(ActiveStatus.ACTIVE)
                .stream()
                .map(brandMapper::toResponse)
                .toList();
    }

    @Override
    public BrandResponse update(Long id, BrandRequest request) throws Exception {
        BrandEntity brand = brandRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND, "Brand not found: " + id));
        brandMapper.updateEntity(request, brand);

        BrandEntity savedBrand = brandRepository.save(brand);

        return brandMapper.toResponse(savedBrand);
    }

    @Override
    public void delete(Long id) throws Exception {
        BrandEntity brand = brandRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND, "Brand not found: " + id));
        if (productRepository.countByBrandId(id) > 0) {
            brand.setStatus(ActiveStatus.INACTIVE);
            brandRepository.save(brand);
        } else {
            brandRepository.delete(brand);
        }
    }

    @Override
    public void deactivate(Long id) throws Exception {
        BrandEntity brand = brandRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND, "Brand not found: " + id));
        brand.setStatus(ActiveStatus.INACTIVE);
        brandRepository.save(brand);
    }

    @Override
    public void activate(Long id) throws Exception {
        BrandEntity brand = brandRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND, "Brand not found: " + id));
        brand.setStatus(ActiveStatus.ACTIVE);
        brandRepository.save(brand);
    }
}
