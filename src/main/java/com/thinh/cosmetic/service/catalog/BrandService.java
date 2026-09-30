package com.thinh.cosmetic.service.catalog;

import com.thinh.cosmetic.domain.dto.request.catalog.BrandRequest;
import com.thinh.cosmetic.domain.dto.response.catalog.BrandResponse;

import java.util.List;

public interface BrandService {
    BrandResponse create(BrandRequest request) throws Exception;

    BrandResponse getById(Long id) throws Exception;

    List<BrandResponse> getAll();

    BrandResponse update(Long id, BrandRequest request) throws Exception;

    void delete(Long id) throws Exception;
}
