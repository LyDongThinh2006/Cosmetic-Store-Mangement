package com.thinh.cosmetic.service.returns;

import com.thinh.cosmetic.domain.dto.request.returns.ReturnRequestDto;
import com.thinh.cosmetic.domain.dto.response.returns.ReturnRequestResponse;
import com.thinh.cosmetic.domain.enums.ReturnStatus;

import java.util.List;

public interface ReturnService {
    ReturnRequestResponse create(ReturnRequestDto request) throws Exception;
    ReturnRequestResponse getById(Long id) throws Exception;
    List<ReturnRequestResponse> getAll();
    List<ReturnRequestResponse> getByCustomer(Long customerId);
    ReturnRequestResponse updateStatus(Long id, ReturnStatus status, Long employeeId) throws Exception;
}
