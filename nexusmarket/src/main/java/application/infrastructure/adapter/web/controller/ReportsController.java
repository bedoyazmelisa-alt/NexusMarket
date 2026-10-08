package application.infrastructure.adapter.web.controller;

import application.domain.port.in.GenerateReportUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/** Exposes {@link GenerateReportUseCase} over HTTP. */
@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final GenerateReportUseCase generateReport;

    public ReportsController(GenerateReportUseCase generateReport) {
        this.generateReport = generateReport;
    }

    @GetMapping(value = "/{reportName}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> generate(@PathVariable String reportName,
                                           @RequestParam LocalDate startDate,
                                           @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(generateReport.generateReport(reportName, startDate, endDate));
    }
}
