package com.cvv.scm_link.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
