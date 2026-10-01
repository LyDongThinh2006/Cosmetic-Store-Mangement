package com.thinh.cosmetic.service.account;

import com.thinh.cosmetic.domain.dto.request.account.EmployeeRequest;
import com.thinh.cosmetic.domain.dto.response.account.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request) throws Exception;
    EmployeeResponse getById(Long id) throws Exception;
    List<EmployeeResponse> getAll();
    EmployeeResponse update(Long id, EmployeeRequest request) throws Exception;
    void deactivate(Long id) throws Exception;
}
