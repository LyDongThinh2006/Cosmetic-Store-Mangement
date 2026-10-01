package com.thinh.cosmetic.service.account;

import com.thinh.cosmetic.domain.dto.request.account.LoginRequest;
import com.thinh.cosmetic.domain.dto.request.account.RegisterRequest;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;

public interface AccountService {
    CustomerResponse register(RegisterRequest request) throws Exception;
    CustomerResponse login(LoginRequest request) throws Exception;
}
