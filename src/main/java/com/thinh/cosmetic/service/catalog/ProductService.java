package com.thinh.cosmetic.service.catalog;

import com.thinh.cosmetic.domain.dto.request.catalog.ProductRequest;
import com.thinh.cosmetic.domain.dto.response.catalog.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request) throws Exception;

    ProductResponse getById(Long id) throws Exception;

    List<ProductResponse> getAll();

    List<ProductResponse> searchAndFilter(
            String keyword,
            Long brandId,
            Long categoryId,
            String skinType,
            String concern,
            String priceRange,
            String sortBy
    );

    ProductResponse update(Long id, ProductRequest request) throws Exception;

    void delete(Long id) throws Exception;
}
