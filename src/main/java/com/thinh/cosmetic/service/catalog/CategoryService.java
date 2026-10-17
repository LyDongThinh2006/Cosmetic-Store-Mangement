package com.thinh.cosmetic.service.catalog;

import com.thinh.cosmetic.domain.dto.request.catalog.CategoryRequest;
import com.thinh.cosmetic.domain.dto.response.catalog.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request) throws Exception;

    CategoryResponse getById(Long id) throws Exception;

    List<CategoryResponse> getAll();

    List<CategoryResponse> getActiveCategories();

    CategoryResponse update(Long id, CategoryRequest request) throws Exception;

    void delete(Long id) throws Exception;

    void deactivate(Long id) throws Exception;

    void activate(Long id) throws Exception;
}
