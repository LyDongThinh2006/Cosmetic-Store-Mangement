package com.thinh.cosmetic.service.store;

import com.thinh.cosmetic.domain.dto.request.store.StoreRequest;
import com.thinh.cosmetic.domain.dto.response.store.StoreResponse;

import java.util.List;

public interface StoreService {
    StoreResponse create(StoreRequest request);
    StoreResponse getById(Long id) throws Exception;
    List<StoreResponse> getAll();
    StoreResponse update(Long id, StoreRequest request) throws Exception;
    void deactivate(Long id) throws Exception;
}
