package com.cvv.scm_link.configuration;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mapbox")
public class BBoxConfig {
    private Map<String, List<Double>> bbox;

    public List<Double> getBbox(String cityCode) {
        if (bbox == null || !bbox.containsKey(cityCode.toUpperCase())) {
            throw new IllegalArgumentException("No BBOX found for city: " + cityCode);
        }
        return bbox.get(cityCode.toUpperCase());
    }
}
