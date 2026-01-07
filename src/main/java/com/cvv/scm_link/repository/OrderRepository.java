package com.cvv.scm_link.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.Order;

@Repository
public interface OrderRepository extends BaseRepository<Order, String> {
    Page<Order> findAllByCreatedBy(String createdBy, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o")
    long countAll();

    @Query(
            """
		SELECT COUNT(o) FROM Order o
		WHERE MONTH(o.createdAt) = MONTH(CURRENT_DATE)
		AND YEAR(o.createdAt) = YEAR(CURRENT_DATE)
	""")
    long countOrdersThisMonth();

    @Query(
            """
		SELECT COUNT(o) FROM Order o
		WHERE MONTH(o.createdAt) = MONTH(CURRENT_DATE) - 1
		AND YEAR(o.createdAt) = YEAR(CURRENT_DATE)
	""")
    long countOrdersLastMonth();

    @Query("SELECT COALESCE(AVG(o.totalAmount),0) FROM Order o")
    double calculateAOV();

    @Query(
            """
		SELECT (COUNT(o) * 1.0 / (SELECT COUNT(o2) FROM Order o2)) * 100
		FROM Order o WHERE o.orderStatus = 'CANCELLED'
	""")
    double calculateCancellationRate();

    @Query("SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o")
    long totalRevenue();

    @Query(
            """
		SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o
		WHERE MONTH(o.createdAt) = MONTH(CURRENT_DATE)
		AND YEAR(o.createdAt) = YEAR(CURRENT_DATE)
	""")
    long totalRevenueThisMonth();

    @Query(
            """
		SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o
		WHERE MONTH(o.createdAt) = MONTH(CURRENT_DATE) - 1
		AND YEAR(o.createdAt) = YEAR(CURRENT_DATE)
	""")
    long totalRevenueLastMonth();

    @Query(
            """
		SELECT
			FUNCTION('MONTH', o.createdAt) AS month,
			SUM(o.totalAmount) AS revenue,
			COUNT(o.id) AS totalOrders
		FROM Order o
		WHERE o.orderStatus = 'COMPLETED'
		AND o.createdAt >= :startDate
		GROUP BY FUNCTION('MONTH', o.createdAt)
		ORDER BY month
	""")
    List<Object[]> getMonthlySales(LocalDateTime startDate);

    @Query(
            """
		SELECT COALESCE(SUM(o.totalAmount), 0)
		FROM Order o
		WHERE o.orderStatus = 'COMPLETED'
		AND o.createdAt >= :start
		AND o.createdAt <= :end
	""")
    Double getRevenueBetween(LocalDateTime start, LocalDateTime end);

    @Query(
            """
		SELECT COALESCE(SUM(o.totalAmount), 0)
		FROM Order o
		WHERE o.orderStatus = 'COMPLETED'
		AND DATE(o.createdAt) = CURRENT_DATE
	""")
    Double getTodayRevenue();

    @Query(
            """
			SELECT o FROM Order o
			JOIN FETCH o.orderItems i
			JOIN FETCH i.product p
			ORDER BY o.createdAt DESC
			""")
    List<Order> findRecentOrders(Pageable pageable);

    // ---- MONTHLY (12 tháng của 1 năm) ----
    @Query(
            """
		SELECT MONTH(o.createdAt) AS month,
			COUNT(o.id) AS sales,
			COALESCE(SUM(o.totalAmount), 0) AS revenue
		FROM Order o
		WHERE YEAR(o.createdAt) = :year
		GROUP BY MONTH(o.createdAt)
		ORDER BY MONTH(o.createdAt)
	""")
    List<Object[]> getMonthlyStatistics(@Param("year") int year);

    // ---- QUARTERLY (4 quý) ----
    @Query(
            """
		SELECT QUARTER(o.createdAt) AS quarter,
			COUNT(o.id) AS sales,
			COALESCE(SUM(o.totalAmount), 0) AS revenue
		FROM Order o
		WHERE YEAR(o.createdAt) = :year
		GROUP BY QUARTER(o.createdAt)
		ORDER BY QUARTER(o.createdAt)
	""")
    List<Object[]> getQuarterlyStatistics(@Param("year") int year);

    // ---- ANNUALLY (theo năm) ----
    @Query(
            """
		SELECT YEAR(o.createdAt) AS year,
			COUNT(o.id) AS sales,
			COALESCE(SUM(o.totalAmount), 0) AS revenue
		FROM Order o
		GROUP BY YEAR(o.createdAt)
		ORDER BY YEAR(o.createdAt)
	""")
    List<Object[]> getAnnualStatistics();
}
