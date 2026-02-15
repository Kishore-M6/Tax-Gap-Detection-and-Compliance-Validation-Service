package com.taxgapdetection.repository;

import com.taxgapdetection.entity.TaxExceptions;
import com.taxgapdetection.helper.Severity;
import com.taxgapdetection.reports.CustomerWiseExceptionSummaryReport;
import com.taxgapdetection.reports.ExceptionSummaryReport;
import com.taxgapdetection.reports.ExceptionSummaryReportBySeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExceptionsRepository extends JpaRepository<TaxExceptions,Long> {
    List<TaxExceptions> findByCustomerId(String customerId);

    List<TaxExceptions> findBySeverity(Severity severity);

    List<TaxExceptions> findByRuleName(String ruleName);

    List<TaxExceptions> findByCustomerIdAndSeverityAndRuleName(
            String customerId,
            Severity severity,
            String ruleName
    );

    @Query(value = "SELECT\n" +
            "            COUNT(e.id) as totalExceptions\n" +
            "        FROM TaxExceptions as e\n")
    List<ExceptionSummaryReport> getTotalExceptionsSummaryReport();

    @Query(value = "SELECT\n" +
            "            e.severity as severity,\n" +
            "            COUNT(e.id) as countBySeverity\n" +
            "        FROM TaxExceptions as e\n" +
            "        GROUP BY e.severity")
    List<ExceptionSummaryReportBySeverity> getExceptionSummaryReportBySeverity();

    @Query(value = " SELECT\n" +
            "        e.customerId as customerId,\n" +
            "        COUNT(e.id) as exceptionCount\n" +
            "    FROM TaxExceptions as e\n" +
            "    GROUP BY e.customerId")
    List<CustomerWiseExceptionSummaryReport> getCustomerWiseExceptionSummaryReport();
}
