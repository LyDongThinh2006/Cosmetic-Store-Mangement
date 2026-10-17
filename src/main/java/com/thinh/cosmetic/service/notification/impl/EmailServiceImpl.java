package com.thinh.cosmetic.service.notification.impl;

import com.thinh.cosmetic.service.notification.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${app.mail.from-name:LUNEA Cosmetics}")
    private String fromName;

    @Value("${app.mail.from-email:no-reply@lunea.vn}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String toEmail, String otpCode, int expiryMinutes) {
        if (toEmail == null || toEmail.isBlank() || !toEmail.contains("@")) {
            log.info(">>> [EMAIL SERVICE] Địa chỉ email '{}' không hợp lệ. Bỏ qua gửi email.", toEmail);
            return;
        }

        if (mailSender == null || mailUsername == null || mailUsername.isBlank()) {
            log.warn(">>> [EMAIL SERVICE] Chưa cấu hình tài khoản gửi thư SMTP (spring.mail.username đang trống).");
            log.info(">>> [EMAIL SERVICE - MÃ OTP NỘI BỘ] Gửi tới [{}]: {}", toEmail, otpCode);
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String senderAddress = (mailUsername != null && mailUsername.contains("@")) ? mailUsername : fromEmail;
            helper.setFrom(new InternetAddress(senderAddress, fromName));
            helper.setTo(toEmail.trim());
            helper.setSubject("LUNEA Cosmetics - Mã xác thực đặt lại mật khẩu: " + otpCode);

            String htmlBody = buildOtpHtml(otpCode, expiryMinutes);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            log.info(">>> [EMAIL SERVICE] Đã gửi thành công email chứa mã OTP đến: {}", toEmail);

        } catch (Exception ex) {
            log.error(">>> [EMAIL SERVICE] Gửi email đến {} thất bại: {}. (Hãy kiểm tra lại App Password hoặc kết nối SMTP)", toEmail, ex.getMessage());
            log.info(">>> [EMAIL SERVICE - FALLBACK] Mã OTP cho {} là: {}", toEmail, otpCode);
        }
    }

    @Override
    public void sendRegistrationOtpEmail(String toEmail, String otpCode, int expiryMinutes) {
        if (toEmail == null || toEmail.isBlank() || !toEmail.contains("@")) {
            log.info(">>> [EMAIL SERVICE] Địa chỉ email '{}' không hợp lệ. Bỏ qua gửi email.", toEmail);
            return;
        }

        if (mailSender == null || mailUsername == null || mailUsername.isBlank()) {
            log.warn(">>> [EMAIL SERVICE] Chưa cấu hình tài khoản gửi thư SMTP (spring.mail.username đang trống).");
            log.info(">>> [EMAIL SERVICE - MÃ OTP ĐĂNG KÝ] Gửi tới [{}]: {}", toEmail, otpCode);
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String senderAddress = (mailUsername != null && mailUsername.contains("@")) ? mailUsername : fromEmail;
            helper.setFrom(new InternetAddress(senderAddress, fromName));
            helper.setTo(toEmail.trim());
            helper.setSubject("LUNEA Cosmetics - Mã kích hoạt đăng ký tài khoản: " + otpCode);

            String htmlBody = buildRegistrationOtpHtml(otpCode, expiryMinutes);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            log.info(">>> [EMAIL SERVICE] Đã gửi thành công email kích hoạt tài khoản đến: {}", toEmail);

        } catch (Exception ex) {
            log.error(">>> [EMAIL SERVICE] Gửi email đăng ký đến {} thất bại: {}. (Hãy kiểm tra lại App Password hoặc kết nối SMTP)", toEmail, ex.getMessage());
            log.info(">>> [EMAIL SERVICE - FALLBACK] Mã OTP đăng ký cho {} là: {}", toEmail, otpCode);
        }
    }

    private String buildRegistrationOtpHtml(String otpCode, int expiryMinutes) {
        return """
            <!DOCTYPE html>
            <html lang="vi">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Kích hoạt tài khoản LUNEA</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #0f0f11; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #e6e6e6;">
                <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color: #0f0f11; padding: 40px 10px;">
                    <tr>
                        <td align="center">
                            <table role="presentation" width="100%%" style="max-width: 520px; background-color: #1a1a1e; border: 1px solid #2d2d35; border-radius: 8px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.5);" cellspacing="0" cellpadding="0">
                                <!-- Header -->
                                <tr>
                                    <td align="center" style="padding: 36px 30px 20px; background: linear-gradient(180deg, #24242a 0%%, #1a1a1e 100%%); border-bottom: 1px solid rgba(212, 175, 55, 0.2);">
                                        <h1 style="margin: 0; font-family: Georgia, serif; font-size: 28px; font-weight: 400; letter-spacing: 4px; color: #d4af37;">L U N E A</h1>
                                        <p style="margin: 6px 0 0; font-size: 10px; letter-spacing: 3px; color: #a0a0a8; text-transform: uppercase;">Haute Parfumerie & Cosmetics</p>
                                    </td>
                                </tr>
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 32px 30px 24px;">
                                        <h2 style="margin: 0 0 16px; font-family: Georgia, serif; font-size: 20px; color: #ffffff; font-weight: 500;">Chào mừng bạn đến với LUNEA</h2>
                                        <p style="margin: 0 0 18px; font-size: 14px; line-height: 1.6; color: #bbbbbf;">
                                            Kính chào quý khách,<br>
                                            Cảm ơn bạn đã lựa chọn trở thành thành viên của chuỗi cửa hàng mỹ phẩm cao cấp LUNEA. Để kích hoạt và hoàn tất quá trình tạo tài khoản, vui lòng sử dụng mã OTP xác minh bên dưới:
                                        </p>
                                        <!-- OTP Box -->
                                        <div style="margin: 28px 0; text-align: center;">
                                            <div style="display: inline-block; background-color: rgba(212, 175, 55, 0.08); border: 1px dashed #d4af37; border-radius: 6px; padding: 16px 32px;">
                                                <span style="font-family: 'Courier New', Courier, monospace; font-size: 32px; font-weight: 700; letter-spacing: 8px; color: #d4af37;">%s</span>
                                            </div>
                                            <p style="margin: 10px 0 0; font-size: 12px; color: #8e8e93;">Mã có hiệu lực trong <b>%d phút</b></p>
                                        </div>
                                        <p style="margin: 0 0 12px; font-size: 13px; line-height: 1.6; color: #a0a0a5;">
                                            ⚠️ <b>Lưu ý:</b> Vui lòng không cung cấp mã OTP này cho bất kỳ ai để bảo vệ tính bảo mật cho tài khoản của bạn.
                                        </p>
                                    </td>
                                </tr>
                                <!-- Footer -->
                                <tr>
                                    <td align="center" style="padding: 20px 30px; background-color: #141416; border-top: 1px solid #24242a;">
                                        <p style="margin: 0; font-size: 11px; color: #66666e; line-height: 1.5;">
                                            © 2026 LUNEA Store Chain Management. Thư này được gửi tự động, vui lòng không trả lời.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(otpCode, expiryMinutes);
    }

    private String buildOtpHtml(String otpCode, int expiryMinutes) {
        return """
            <!DOCTYPE html>
            <html lang="vi">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Khôi phục mật khẩu LUNEA</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #0f0f11; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #e6e6e6;">
                <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color: #0f0f11; padding: 40px 10px;">
                    <tr>
                        <td align="center">
                            <table role="presentation" width="100%%" style="max-width: 520px; background-color: #1a1a1e; border: 1px solid #2d2d35; border-radius: 8px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.5);" cellspacing="0" cellpadding="0">
                                <!-- Header -->
                                <tr>
                                    <td align="center" style="padding: 36px 30px 20px; background: linear-gradient(180deg, #24242a 0%%, #1a1a1e 100%%); border-bottom: 1px solid rgba(212, 175, 55, 0.2);">
                                        <h1 style="margin: 0; font-family: Georgia, serif; font-size: 28px; font-weight: 400; letter-spacing: 4px; color: #d4af37;">L U N E A</h1>
                                        <p style="margin: 6px 0 0; font-size: 10px; letter-spacing: 3px; color: #a0a0a8; text-transform: uppercase;">Haute Parfumerie & Cosmetics</p>
                                    </td>
                                </tr>
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 32px 30px 24px;">
                                        <h2 style="margin: 0 0 16px; font-family: Georgia, serif; font-size: 20px; color: #ffffff; font-weight: 500;">Xác thực khôi phục mật khẩu</h2>
                                        <p style="margin: 0 0 18px; font-size: 14px; line-height: 1.6; color: #bbbbbf;">
                                            Kính chào quý khách,<br>
                                            Hệ thống vừa nhận được yêu cầu thiết lập lại mật khẩu cho tài khoản LUNEA liên kết với email này. Vui lòng sử dụng mã xác thực bảo mật (OTP) dưới đây để tiếp tục:
                                        </p>
                                        <!-- OTP Box -->
                                        <div style="margin: 28px 0; text-align: center;">
                                            <div style="display: inline-block; background-color: rgba(212, 175, 55, 0.08); border: 1px dashed #d4af37; border-radius: 6px; padding: 16px 32px;">
                                                <span style="font-family: 'Courier New', Courier, monospace; font-size: 32px; font-weight: 700; letter-spacing: 8px; color: #d4af37;">%s</span>
                                            </div>
                                            <p style="margin: 10px 0 0; font-size: 12px; color: #8e8e93;">Mã có hiệu lực trong <b>%d phút</b></p>
                                        </div>
                                        <p style="margin: 0 0 12px; font-size: 13px; line-height: 1.6; color: #a0a0a5;">
                                            ⚠️ <b>Lưu ý quan trọng:</b> Không chia sẻ mã OTP này cho bất kỳ ai, kể cả nhân viên hỗ trợ của LUNEA.
                                        </p>
                                        <p style="margin: 0; font-size: 13px; line-height: 1.6; color: #8e8e93;">
                                            Nếu quý khách không yêu cầu khôi phục mật khẩu, vui lòng bỏ qua thư này hoặc đổi mật khẩu tài khoản ngay để bảo vệ thông tin.
                                        </p>
                                    </td>
                                </tr>
                                <!-- Footer -->
                                <tr>
                                    <td align="center" style="padding: 20px 30px; background-color: #141416; border-top: 1px solid #24242a;">
                                        <p style="margin: 0; font-size: 11px; color: #66666e; line-height: 1.5;">
                                            © 2026 LUNEA Store Chain Management. Thư này được gửi tự động, vui lòng không trả lời.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(otpCode, expiryMinutes);
    }
}
