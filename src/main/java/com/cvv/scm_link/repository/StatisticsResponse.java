package com.cvv.scm_link.repository;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    List<String> labels;
    List<Long> sales;
    List<Double> revenue;
}
