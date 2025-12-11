package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.stats.DashboardResponse;
import com.cvv.scm_link.dto.response.stats.MonthlySalesDTO;
import com.cvv.scm_link.dto.response.stats.MonthlyTargetDTO;
import com.cvv.scm_link.service.DashboardService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {
    DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/overview")
    public APIResponse<DashboardResponse> getOverview() {
        return APIResponse.<DashboardResponse>builder()
                .result(dashboardService.getDashboardData())
                .build();
    }

    @GetMapping("/monthly-sales")
    public APIResponse<MonthlySalesDTO> getMonthlySales() {
        return APIResponse.<MonthlySalesDTO>builder()
                .result(dashboardService.getMonthlySales())
                .build();
    }

    @GetMapping("/monthly-target")
    public MonthlyTargetDTO getMonthlyTarget() {
        return dashboardService.getMonthlyTarget();
    }
}
