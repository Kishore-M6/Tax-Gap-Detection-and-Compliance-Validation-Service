package com.taxgapdetection.repository;

import com.taxgapdetection.reports.CustomerTaxSummaryReport;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.helper.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Optional<Transaction> findTopByCustomerIdAndTransactionTypeOrderByDateDesc(String customerId, TransactionType type);

    @Query(value = "SELECT\n " +
            "            t.customerId as customerId,\n" +
            "            SUM(t.amount) as totalAmount,\n" +
            "            SUM(t.reportedTax) as totalReportedTax,\n" +
            "            SUM(t.expectedTax) as totalExpectedTax,\n" +
            "            SUM(t.taxGap) as totalTaxGap,\n" +
            "\n" +
            "            (100.0 -\n" +
            "            (SUM(CASE\n" +
            "                    WHEN t.complianceStatus = 'NON_COMPLIANT'\n" +
            "                    THEN 1 ELSE 0\n" +
            "                 END) * 100.0 / COUNT(t.customerId)))\n" +
            "            as complianceScore\n " +
            "\n" +
            "        FROM Transaction as t\n" +
            "        GROUP BY t.customerId ")
    List<CustomerTaxSummaryReport> getCustomerTaxSummaryReport();


    Optional<Transaction> findByTransactionId(String txnId);
}
