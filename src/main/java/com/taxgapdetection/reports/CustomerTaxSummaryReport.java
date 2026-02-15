package com.taxgapdetection.reports;

import java.math.BigDecimal;

public interface CustomerTaxSummaryReport {
    String getCustomerId();
    BigDecimal getTotalAmount();
    BigDecimal getTotalReportedTax();
    BigDecimal getTotalExpectedTax();
    BigDecimal getTotalTaxGap();
    Double getComplianceScore();

}
