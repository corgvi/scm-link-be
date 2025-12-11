package com.cvv.scm_link.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfig {

    private String apiKey;
    private String apiSecret;
    private String cloudName;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> cloudinaryConfig = new HashMap<>();
        cloudinaryConfig.put("cloud_name", cloudName);
        cloudinaryConfig.put("api_key", apiKey);
        cloudinaryConfig.put("api_secret", apiSecret);
        cloudinaryConfig.put("secure", true);
        cloudinaryConfig.put("timeout", 60000);
        cloudinaryConfig.put("max_requests", 50);

        return new Cloudinary(cloudinaryConfig);
    }
}
