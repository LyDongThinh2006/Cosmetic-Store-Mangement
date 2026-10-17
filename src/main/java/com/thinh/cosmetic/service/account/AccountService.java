package com.thinh.cosmetic.service.account;

import com.thinh.cosmetic.domain.dto.request.account.LoginRequest;
import com.thinh.cosmetic.domain.dto.request.account.RegisterRequest;
import com.thinh.cosmetic.domain.dto.request.account.ResetPasswordRequest;
import com.thinh.cosmetic.domain.dto.request.account.SendOtpRequest;
import com.thinh.cosmetic.domain.dto.request.account.VerifyOtpRequest;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.domain.dto.response.account.LoginResult;
import com.thinh.cosmetic.domain.dto.response.account.SendOtpResponse;

public interface AccountService {
    CustomerResponse register(RegisterRequest request);
    LoginResult login(LoginRequest request);
    void resetPassword(ResetPasswordRequest request);
    SendOtpResponse sendResetPasswordOtp(SendOtpRequest request);
    void verifyResetPasswordOtp(VerifyOtpRequest request);
}
