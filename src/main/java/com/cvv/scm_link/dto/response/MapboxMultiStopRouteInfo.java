package com.cvv.scm_link.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapboxMultiStopRouteInfo {
    private double totalDistanceKm;
    private double totalDurationMinutes;
    private List<Integer> waypointOrder; // thứ tự tối ưu
}
