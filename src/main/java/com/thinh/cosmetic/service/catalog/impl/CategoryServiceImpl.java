package com.thinh.cosmetic.service.catalog.impl;

import com.thinh.cosmetic.domain.dto.request.catalog.CategoryRequest;
import com.thinh.cosmetic.domain.dto.response.catalog.CategoryResponse;
import com.thinh.cosmetic.domain.entity.catalog.CategoryEntity;
import com.thinh.cosmetic.mapper.catalog.CategoryMapper;
import com.thinh.cosmetic.repository.catalog.CategoryRepository;
import com.thinh.cosmetic.service.catalog.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) throws Exception {
        CategoryEntity category = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            CategoryEntity parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() ->
                            new RuntimeException("Parent category not found: " + request.getParentId())
                    );

            category.setParent(parent);
        }

        CategoryEntity savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) throws Exception {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                "Category not found: " + id
                        ));
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) throws Exception {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                "Category not found: " + id
                        ));
        categoryMapper.updateEntity(request, category);

        CategoryEntity savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public void delete(Long id) throws Exception {
        categoryRepository.deleteById(id);
    }
}
