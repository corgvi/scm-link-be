package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.repository.StatisticsResponse;
import com.cvv.scm_link.service.StatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public APIResponse<StatisticsResponse> getStatistics(@RequestParam(defaultValue = "monthly") String mode) {
        StatisticsResponse stats = statisticsService.getStatistics(mode);
        return APIResponse.<StatisticsResponse>builder().result(stats).build();
    }
}
