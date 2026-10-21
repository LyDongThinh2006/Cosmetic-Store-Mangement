package com.thinh.cosmetic.rest.catalog;

import com.thinh.cosmetic.service.storage.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadRestController {

    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('CATALOG_MANAGE', 'ROLE_PRODUCT_MANAGER', 'ROLE_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "lunea/products") String folder
    ) {
        Map<String, Object> result = cloudinaryService.uploadImage(file, folder);
        return ResponseEntity.ok(result);
    }
}
