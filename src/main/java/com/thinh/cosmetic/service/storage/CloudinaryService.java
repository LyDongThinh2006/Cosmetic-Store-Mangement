package com.thinh.cosmetic.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    public Map<String, Object> uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Vui lòng chọn một file ảnh hợp lệ để tải lên");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/"))) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Định dạng file không hợp lệ. Chỉ chấp nhận file ảnh (PNG, JPG, WEBP, JPEG)");
        }

        // Check file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Dung lượng file ảnh không được vượt quá 5MB");
        }

        if (apiKey == null || apiKey.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Chưa cấu hình Cloudinary API Key. Vui lòng cung cấp Cloud Name, API Key và API Secret trong file cấu hình.");
        }

        try {
            String targetFolder = (folder != null && !folder.isBlank()) ? folder : "lunea/products";
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", targetFolder,
                    "resource_type", "image",
                    "overwrite", true
            ));

            String url = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            log.info(">>> [Cloudinary] Tải ảnh lên thành công. Public ID: {}, URL: {}", publicId, url);
            return Map.of(
                    "url", url,
                    "publicId", publicId,
                    "format", String.valueOf(uploadResult.getOrDefault("format", "")),
                    "width", uploadResult.getOrDefault("width", 0),
                    "height", uploadResult.getOrDefault("height", 0)
            );
        } catch (IOException e) {
            log.error(">>> [Cloudinary] Lỗi khi upload ảnh lên Cloudinary: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Không thể tải ảnh lên Cloudinary: " + e.getMessage());
        }
    }

    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        if (apiKey == null || apiKey.isBlank()) return;

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info(">>> [Cloudinary] Đã xóa ảnh có publicId: {}", publicId);
        } catch (Exception e) {
            log.warn(">>> [Cloudinary] Không thể xóa ảnh trên Cloudinary: {}", e.getMessage());
        }
    }
}
