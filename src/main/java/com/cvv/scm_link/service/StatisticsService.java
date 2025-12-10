package com.cvv.scm_link.service;

import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.repository.OrderRepository;
import com.cvv.scm_link.repository.StatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;

    public StatisticsResponse getStatistics(String mode) {

        switch (mode.toLowerCase()) {

            case "monthly":
                return getMonthlyStatistics();

            case "quarterly":
                return getQuarterlyStatistics();

            case "annually":
                return getAnnualStatistics();

            default:
                throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
    }


    // ============================
    //  MONTHLY STATISTICS (12 tháng)
    // ============================
    private StatisticsResponse getMonthlyStatistics() {
        int year = LocalDate.now().getYear();
        List<Object[]> result = orderRepository.getMonthlyStatistics(year);

        Map<Integer, Long> salesMap = new HashMap<>();
        Map<Integer, Double> revenueMap = new HashMap<>();

        result.forEach(r -> {
            int month = ((Number) r[0]).intValue();
            long sales = ((Number) r[1]).longValue();
            double revenue = ((Number) r[2]).doubleValue();

            salesMap.put(month, sales);
            revenueMap.put(month, revenue);
        });

        List<String> labels = List.of("Jan","Feb","Mar","Apr","May","Jun",
                "Jul","Aug","Sep","Oct","Nov","Dec");

        List<Long> sales = new ArrayList<>();
        List<Double> revenue = new ArrayList<>();

        for (int m = 1; m <= 12; m++) {
            sales.add(salesMap.getOrDefault(m, 0L));
            revenue.add(revenueMap.getOrDefault(m, 0.0));
        }

        return new StatisticsResponse(labels, sales, revenue);
    }


    // =============================
    // QUARTERLY STATISTICS (4 quý)
    // =============================
    private StatisticsResponse getQuarterlyStatistics() {
        int year = LocalDate.now().getYear();
        List<Object[]> result = orderRepository.getQuarterlyStatistics(year);

        Map<Integer, Long> salesMap = new HashMap<>();
        Map<Integer, Double> revenueMap = new HashMap<>();

        result.forEach(r -> {
            int quarter = ((Number) r[0]).intValue();
            long sales = ((Number) r[1]).longValue();
            double revenue = ((Number) r[2]).doubleValue();

            salesMap.put(quarter, sales);
            revenueMap.put(quarter, revenue);
        });

        List<String> labels = List.of("Q1", "Q2", "Q3", "Q4");

        List<Long> sales = new ArrayList<>();
        List<Double> revenue = new ArrayList<>();

        for (int q = 1; q <= 4; q++) {
            sales.add(salesMap.getOrDefault(q, 0L));
            revenue.add(revenueMap.getOrDefault(q, 0.0));
        }

        return new StatisticsResponse(labels, sales, revenue);
    }


    // ============================
    //  ANNUAL STATISTICS (theo năm)
    // ============================
    private StatisticsResponse getAnnualStatistics() {
        List<Object[]> result = orderRepository.getAnnualStatistics();

        List<String> labels = new ArrayList<>();
        List<Long> sales = new ArrayList<>();
        List<Double> revenue = new ArrayList<>();

        result.forEach(r -> {
            int year = ((Number) r[0]).intValue();
            long sale = ((Number) r[1]).longValue();
            double rev = ((Number) r[2]).doubleValue();

            labels.add(String.valueOf(year));
            sales.add(sale);
            revenue.add(rev);
        });

        return new StatisticsResponse(labels, sales, revenue);
    }
}

