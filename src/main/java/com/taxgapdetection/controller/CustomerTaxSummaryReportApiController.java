package com.taxgapdetection.controller;

import com.taxgapdetection.reports.CustomerTaxSummaryReport;
import com.taxgapdetection.service.CustomerTaxSummaryReportApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reports")
@RequiredArgsConstructor
public class CustomerTaxSummaryReportApiController {
    private final CustomerTaxSummaryReportApiService customerTaxSummaryReportService;

    @GetMapping(value = "/customer-tax-summary-report")
    public List<CustomerTaxSummaryReport> getCustomerTaxSummaryReport(){
        return customerTaxSummaryReportService.generateCustomerTaxReport();
    }

}
