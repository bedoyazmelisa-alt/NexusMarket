package application.service;

import application.domain.enums.OrderStatus;
import application.domain.model.Order;
import application.domain.port.in.GenerateReportUseCase;
import application.domain.port.out.OrderRepository;
import application.domain.valueobject.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

/**
 * Application service for administrative reporting. Implements
 * {@link GenerateReportUseCase}.
 *
 * <p>Supported reports:</p>
 * <ul>
 *   <li>{@code orders} - orders created within the period, grouped by status
 *   and totalled per currency.</li>
 * </ul>
 */
public class ReportService implements GenerateReportUseCase {

    private static final String ORDERS_REPORT = "orders";

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public String generateReport(String reportName, LocalDate startDate, LocalDate endDate) {
        if (reportName == null || reportName.isBlank()) {
            throw new IllegalArgumentException("Report name must not be blank");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates must not be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }

        String normalized = reportName.trim().toLowerCase(Locale.ROOT);
        if (ORDERS_REPORT.equals(normalized)) {
            return ordersReport(startDate, endDate);
        }
        throw new IllegalArgumentException("Unknown report: " + reportName);
    }

    private String ordersReport(LocalDate startDate, LocalDate endDate) {
        // The period is inclusive on both sides, so the query bound is exclusive.
        List<Order> orders = orderRepository.findByCreatedAtBetween(
                startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        Map<OrderStatus, Long> byStatus = new EnumMap<>(OrderStatus.class);
        Map<String, BigDecimal> totalsByCurrency = new TreeMap<>();
        for (Order order : orders) {
            byStatus.merge(order.getStatus(), 1L, Long::sum);
            Money total = order.getTotalAmount();
            if (total != null) {
                totalsByCurrency.merge(total.getCurrency(), total.getAmount(), BigDecimal::add);
            }
        }

        return "NexusMarket orders report\n"
                + "Period: " + startDate + " to " + endDate + "\n"
                + "Orders: " + orders.size() + "\n"
                + "By status: " + formatStatusCounts(byStatus) + "\n"
                + "Total amount: " + formatTotals(totalsByCurrency);
    }

    private String formatStatusCounts(Map<OrderStatus, Long> counts) {
        if (counts.isEmpty()) {
            return "none";
        }
        StringJoiner joiner = new StringJoiner(", ");
        for (OrderStatus status : OrderStatus.values()) {
            Long count = counts.get(status);
            if (count != null) {
                joiner.add(status + "=" + count);
            }
        }
        return joiner.toString();
    }

    private String formatTotals(Map<String, BigDecimal> totals) {
        if (totals.isEmpty()) {
            return "none";
        }
        StringJoiner joiner = new StringJoiner(", ");
        totals.forEach((currency, amount) -> joiner.add(currency + " " + amount.toPlainString()));
        return joiner.toString();
    }
}
