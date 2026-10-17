package com.thinh.cosmetic.service.account;

import com.thinh.cosmetic.domain.dto.request.account.ResetPasswordRequest;
import com.thinh.cosmetic.domain.dto.request.account.SendOtpRequest;
import com.thinh.cosmetic.domain.dto.request.account.VerifyOtpRequest;
import com.thinh.cosmetic.domain.dto.response.account.SendOtpResponse;
import com.thinh.cosmetic.domain.entity.account.PasswordResetOtpEntity;

public interface OtpService {

    SendOtpResponse sendPasswordResetOtp(SendOtpRequest request);

    PasswordResetOtpEntity verifyOtp(VerifyOtpRequest request);

    void completePasswordReset(ResetPasswordRequest request);

    String maskIdentifier(String identifier);
}
