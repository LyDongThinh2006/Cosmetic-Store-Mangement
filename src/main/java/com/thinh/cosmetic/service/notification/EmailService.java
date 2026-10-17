package com.thinh.cosmetic.service.notification;

public interface EmailService {

    void sendOtpEmail(String toEmail, String otpCode, int expiryMinutes);

    void sendRegistrationOtpEmail(String toEmail, String otpCode, int expiryMinutes);
}
