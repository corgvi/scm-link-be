package com.cvv.scm_link.dto.response;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MapboxDirectionResponse {
    List<Route> routes;

    @Data
    public static class Route {
        double distance;
        double duration;
    }
}
