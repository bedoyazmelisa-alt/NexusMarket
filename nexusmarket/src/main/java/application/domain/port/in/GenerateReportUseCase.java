package application.domain.port.in;

import java.time.LocalDate;

/**
 * Input port: generates administrative reports from domain data.
 */
public interface GenerateReportUseCase {

    String generateReport(String reportName, LocalDate startDate, LocalDate endDate);
}