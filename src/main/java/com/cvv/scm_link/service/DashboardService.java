package com.cvv.scm_link.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.response.stats.*;
import com.cvv.scm_link.repository.DeliveryRepository;
import com.cvv.scm_link.repository.OrderRepository;
import com.cvv.scm_link.repository.UserRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardService {
    UserRepository userRepository;
    OrderRepository orderRepository;
    DeliveryRepository deliveryRepository;

    public DashboardService(
            UserRepository userRepository, OrderRepository orderRepository, DeliveryRepository deliveryRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public DashboardResponse getDashboardData() {
        return DashboardResponse.builder()
                .customerStats(buildCustomerStats())
                .orderStats(buildOrderStats())
                .revenueStats(buildRevenueStats())
                .deliveryStats(buildDeliveryStats())
                .build();
    }

    public MonthlySalesDTO getMonthlySales() {
        LocalDateTime start = LocalDate.now().minusMonths(11).withDayOfMonth(1).atStartOfDay();

        List<Object[]> raw = orderRepository.getMonthlySales(start);

        Map<Integer, Long> revenueMap = new HashMap<>();
        Map<Integer, Integer> ordersMap = new HashMap<>();

        for (Object[] row : raw) {
            Integer month = (Integer) row[0];
            Long revenue = (Long) row[1];
            Long orders = (Long) row[2];

            revenueMap.put(month, revenue);
            ordersMap.put(month, orders.intValue());
        }

        List<String> months = new ArrayList<>();
        List<Long> revenueList = new ArrayList<>();
        List<Integer> orderList = new ArrayList<>();
        List<Double> growthList = new ArrayList<>();

        long lastRevenue = 0;

        for (int i = 1; i <= 12; i++) {
            months.add(Month.of(i).name().substring(0, 3));

            long r = revenueMap.getOrDefault(i, 0L);
            int o = ordersMap.getOrDefault(i, 0);

            revenueList.add(r);
            orderList.add(o);

            if (lastRevenue == 0) {
                growthList.add(0.0);
            } else {
                double growth = ((double) (r - lastRevenue) / lastRevenue) * 100;
                growthList.add(growth);
            }

            lastRevenue = r;
        }

        return MonthlySalesDTO.builder()
                .months(months)
                .revenue(revenueList)
                .orders(orderList)
                .growthRate(growthList)
                .build();
    }

    public MonthlyTargetDTO getMonthlyTarget() {

        // 1. Tính khoảng thời gian tháng hiện tại
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        // 2. Khoảng thời gian tháng trước
        YearMonth previousMonth = currentMonth.minusMonths(1);
        LocalDateTime startOfPrevMonth = previousMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfPrevMonth = previousMonth.atEndOfMonth().atTime(23, 59, 59);

        // 3. Query doanh thu
        double revenueThisMonth = orderRepository.getRevenueBetween(startOfMonth, endOfMonth);
        double revenueLastMonth = orderRepository.getRevenueBetween(startOfPrevMonth, endOfPrevMonth);
        double todayRevenue = orderRepository.getTodayRevenue();

        // 4. Mục tiêu: tự fix hoặc lấy từ DB (tạm fix)
        double monthlyTarget = 2000000000;

        // 5. Progress %
        double progress = monthlyTarget > 0 ? (revenueThisMonth / monthlyTarget) * 100 : 0;

        // 6. Growth %
        double growth = revenueLastMonth > 0
                ? ((revenueThisMonth - revenueLastMonth) / revenueLastMonth) * 100
                : 100; // nếu tháng trước = 0 → tăng trưởng 100%

        return MonthlyTargetDTO.builder()
                .monthlyTarget(monthlyTarget)
                .revenueThisMonth(revenueThisMonth)
                .todayRevenue(todayRevenue)
                .progress(progress)
                .growth(growth)
                .build();
    }

    private CustomerStats buildCustomerStats() {
        long total = userRepository.countCustomers();
        long newThisMonth = userRepository.countNewCustomersThisMonth();

        long lastMonth = userRepository.countNewCustomersLastMonth();
        double growth = calcGrowth(newThisMonth, lastMonth);

        return CustomerStats.builder()
                .totalCustomers(total)
                .newCustomersThisMonth(newThisMonth)
                .growthRate(growth)
                .build();
    }

    private OrderStats buildOrderStats() {
        long total = orderRepository.countAll();
        long thisMonth = orderRepository.countOrdersThisMonth();
        long lastMonth = orderRepository.countOrdersLastMonth();

        double growth = calcGrowth(thisMonth, lastMonth);
        double cancelRate = orderRepository.calculateCancellationRate();
        double aov = orderRepository.calculateAOV();

        return OrderStats.builder()
                .totalOrders(total)
                .ordersThisMonth(thisMonth)
                .growthRate(growth)
                .cancellationRate(cancelRate)
                .averageOrderValue(aov)
                .build();
    }

    private RevenueStats buildRevenueStats() {
        long totalRevenue = orderRepository.totalRevenue();
        long thisMonth = orderRepository.totalRevenueThisMonth();
        long lastMonth = orderRepository.totalRevenueLastMonth();

        double growth = calcGrowth(thisMonth, lastMonth);

        return RevenueStats.builder()
                .totalRevenue(totalRevenue)
                .revenueThisMonth(thisMonth)
                .growthRate(growth)
                .build();
    }

    private DeliveryStats buildDeliveryStats() {
        long delivering = deliveryRepository.countByDeliveryStatus("DELIVERING");
        long completed = deliveryRepository.countByDeliveryStatus("DELIVERY_COMPLETED");

        double onTimeRate = deliveryRepository.calculateOnTimeRate();

        return DeliveryStats.builder()
                .deliveringCount(delivering)
                .completedCount(completed)
                .onTimeDeliveryRate(onTimeRate)
                .build();
    }

    private double calcGrowth(long current, long previous) {
        if (previous == 0) return current > 0 ? 100 : 0;
        return ((double) (current - previous) / previous) * 100;
    }
}
