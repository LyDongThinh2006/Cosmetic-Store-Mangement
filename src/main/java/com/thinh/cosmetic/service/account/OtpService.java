package com.thinh.cosmetic.service.account;

import com.thinh.cosmetic.domain.dto.request.account.ResetPasswordRequest;
import com.thinh.cosmetic.domain.dto.request.account.SendOtpRequest;
import com.thinh.cosmetic.domain.dto.request.account.VerifyOtpRequest;
import com.thinh.cosmetic.domain.dto.response.account.SendOtpResponse;
import com.thinh.cosmetic.domain.entity.account.PasswordResetOtpEntity;
import com.thinh.cosmetic.domain.entity.account.RegistrationOtpEntity;

public interface OtpService {

    SendOtpResponse sendPasswordResetOtp(SendOtpRequest request);

    PasswordResetOtpEntity verifyOtp(VerifyOtpRequest request);

    void completePasswordReset(ResetPasswordRequest request);

    SendOtpResponse sendRegistrationOtp(SendOtpRequest request);

    RegistrationOtpEntity verifyRegistrationOtp(String email, String otp);

    void completeRegistrationOtp(String email, String otp);

    String maskIdentifier(String identifier);
}
