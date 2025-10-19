package com.cvv.scm_link.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.cvv.scm_link.configuration.BBoxConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;

import com.cvv.scm_link.dto.response.MapboxDirectionResponse;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class MapboxService {

    @Value("${mapbox.access.token}")
    String mapboxApiKey;

    final BBoxConfig bBoxConfig;

    final WebClient mapboxWebClient;

    public MapboxService(BBoxConfig bBoxConfig, WebClient.Builder webClientBuilder) {
        this.bBoxConfig = bBoxConfig;
        this.mapboxWebClient =
                webClientBuilder.baseUrl("https://api.mapbox.com/").build();
    }

    public Mono<Double> getDistanceKm(double originLat, double originLon, double destLat, double destLon) {
        String url = String.format(
                "/directions/v5/mapbox/driving/%f,%f;%f,%f?access_token=%s",
                originLon, originLat, destLon, destLat, mapboxApiKey);

        return mapboxWebClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(MapboxDirectionResponse.class)
                .map(response -> {
                    if (response.getRoutes() == null || response.getRoutes().isEmpty()) {
                        throw new AppException(ErrorCode.ROUTE_NOT_FOUND);
                    }
                    double distanceMeters = response.getRoutes().get(0).getDistance();
                    log.info("Distance calculated: {} km", distanceMeters / 1000);
                    return distanceMeters / 1000; // Convert m -> km
                })
                .defaultIfEmpty(0.0);
    }

    public Mono<double[]> getCoordinatesFromAddress(String address, String cityCode) {
        List<Double> bbox = bBoxConfig.getBbox(cityCode);
        String bboxParam = String.format("%f,%f,%f,%f", bbox.get(0), bbox.get(1), bbox.get(2), bbox.get(3));

        String url = String.format(
                "/geocoding/v5/mapbox.places/%s.json?access_token=%s&limit=1&country=VN&types=address,place&bbox=%s",
                UriUtils.encodePath(address, StandardCharsets.UTF_8),
                mapboxApiKey,
                bboxParam
        );

        return mapboxWebClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    JsonNode features = json.get("features");
                    if (features == null || features.isEmpty()) {
                        throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
                    }
                    JsonNode center = features.get(0).get("center");
                    double lon = center.get(0).asDouble();
                    double lat = center.get(1).asDouble();
                    log.info("Resolved address: {} => {}, {} + features: {}", address, lat, lon, features);
                    return new double[]{lat, lon};
                })
                .defaultIfEmpty(new double[]{0.0, 0.0});
    }
}
