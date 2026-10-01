package com.thinh.cosmetic.service.purchase.impl;

import com.thinh.cosmetic.domain.dto.request.purchase.SupplierRequest;
import com.thinh.cosmetic.domain.dto.response.purchase.SupplierResponse;
import com.thinh.cosmetic.domain.entity.purchase.SupplierEntity;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.mapper.purchase.SupplierMapper;
import com.thinh.cosmetic.repository.purchase.SupplierRepository;
import com.thinh.cosmetic.service.purchase.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponse create(SupplierRequest request) {
        SupplierEntity entity = supplierMapper.toEntity(request);
        if (entity.getStatus() == null) entity.setStatus(ActiveStatus.ACTIVE);
        return supplierMapper.toResponse(supplierRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getById(Long id) throws Exception {
        return supplierMapper.toResponse(supplierRepository.findById(id)
                .orElseThrow(() -> new Exception("Supplier not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAll() {
        return supplierRepository.findAll().stream().map(supplierMapper::toResponse).toList();
    }

    @Override
    public SupplierResponse update(Long id, SupplierRequest request) throws Exception {
        SupplierEntity entity = supplierRepository.findById(id)
                .orElseThrow(() -> new Exception("Supplier not found: " + id));
        supplierMapper.updateEntity(request, entity);
        return supplierMapper.toResponse(supplierRepository.save(entity));
    }

    @Override
    public void deactivate(Long id) throws Exception {
        SupplierEntity entity = supplierRepository.findById(id)
                .orElseThrow(() -> new Exception("Supplier not found: " + id));
        entity.setStatus(ActiveStatus.INACTIVE);
        supplierRepository.save(entity);
    }
}
