package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/uploads")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryController {

    CloudinaryService cloudinaryService;

    public CloudinaryController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    // Upload đơn lẻ
    @PostMapping("/upload")
    public APIResponse<String> uploadSingle(@RequestParam("file") MultipartFile file) throws IOException {
            String url = cloudinaryService.uploadAndGetUrl(file);
            return APIResponse.<String>builder().result(url).build();
    }

    // Upload với transform (ví dụ: resize 800x600)
    @PostMapping("/upload/transform")
    public ResponseEntity<Map<String, Object>> uploadWithTransform(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "800") int width,
            @RequestParam(defaultValue = "600") int height) {
        try {
            String url = cloudinaryService.uploadWithTransform(file, width, height);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{publicId}")
    public APIResponse<Map> deleteFile(@PathVariable String publicId) throws IOException {
        Map result = cloudinaryService.delete(publicId);
        return APIResponse.<Map>builder().result(result).build();
    }

}
