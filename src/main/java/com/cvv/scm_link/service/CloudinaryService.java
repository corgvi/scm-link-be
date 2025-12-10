package com.cvv.scm_link.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {

    Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Upload cơ bản từ MultipartFile, trả về Map result
    public Map upload(MultipartFile file) throws IOException {
        Uploader uploader = cloudinary.uploader();

        // Builder pattern cho params (new in v2.x)
        Map<String, Object> params = Map.of(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true,
                "resource_type", "auto"  // Tự detect image/video
        );

        // Upload với error handling
        try {
            return uploader.upload(file.getBytes(), params);
        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
    }

    // Upload và trả về secure URL (dùng cho frontend)
    public String uploadAndGetUrl(MultipartFile file) throws IOException {
        String publicId = "images" + System.currentTimeMillis();
        Map<String, Object> params = Map.of(
                "public_id", publicId,
                "resource_type", "auto"
        );

        Map result = cloudinary.uploader().upload(file.getBytes(), params);
        if (result.containsKey("error")) {
            throw new RuntimeException("Cloudinary error: " + result.get("error"));
        }
        return (String) result.get("secure_url");
    }

    // Upload với transformation (resize, optimize - new features v2.x)
    public String uploadWithTransform(MultipartFile file, int width, int height) throws IOException {
        Uploader uploader = cloudinary.uploader();

        Map<String, Object> params = Map.of(
                "transformation", Map.of(
                        "width", width,
                        "height", height,
                        "crop", "fill",
                        "quality", "auto",
                        "fetch_format", "auto"  // Tối ưu format (WebP/AVIF)
                ),
                "use_filename", true,
                "overwrite", true
        );

        Map result = uploader.upload(file.getBytes(), params);
        return (String) result.get("secure_url");
    }

    // Xóa file theo public_id
    public Map delete(String publicId) throws IOException {
        try {
            return cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: " + e.getMessage(), e);
        }
    }
}
