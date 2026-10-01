package com.thinh.cosmetic.service.store.impl;

import com.thinh.cosmetic.domain.dto.request.store.StoreRequest;
import com.thinh.cosmetic.domain.dto.response.store.StoreResponse;
import com.thinh.cosmetic.domain.entity.store.StoreEntity;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.mapper.store.StoreMapper;
import com.thinh.cosmetic.repository.store.StoreRepository;
import com.thinh.cosmetic.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    public StoreResponse create(StoreRequest request) {
        StoreEntity store = storeMapper.toEntity(request);
        if (store.getStatus() == null) store.setStatus(ActiveStatus.ACTIVE);
        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    @Transactional(readOnly = true)
    public StoreResponse getById(Long id) throws Exception {
        return storeMapper.toResponse(storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponse> getAll() {
        return storeRepository.findAll().stream().map(storeMapper::toResponse).toList();
    }

    @Override
    public StoreResponse update(Long id, StoreRequest request) throws Exception {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found: " + id));
        storeMapper.updateEntity(request, store);
        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    public void deactivate(Long id) throws Exception {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found: " + id));
        store.setStatus(ActiveStatus.INACTIVE);
        storeRepository.save(store);
    }
}
