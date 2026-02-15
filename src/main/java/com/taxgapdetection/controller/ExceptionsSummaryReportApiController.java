package com.taxgapdetection.controller;

import com.taxgapdetection.reports.CustomerWiseExceptionSummaryReport;
import com.taxgapdetection.reports.ExceptionSummaryReport;
import com.taxgapdetection.reports.ExceptionSummaryReportBySeverity;
import com.taxgapdetection.service.ExceptionsSummaryReportApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reports")
@RequiredArgsConstructor
public class ExceptionsSummaryReportApiController {
    private final ExceptionsSummaryReportApiService exceptionsSummaryReportApiService;

    @GetMapping(value = "/total-exceptions")
    public List<ExceptionSummaryReport> getTotalExceptions() {
       return exceptionsSummaryReportApiService.getTotalExceptions();
    }
    @GetMapping(value = "/count-of-exceptions-by-severity")
    public List<ExceptionSummaryReportBySeverity> getCountOfExceptionsBySeverity(){
        return exceptionsSummaryReportApiService.getCountOfExceptionsBySeverity();
    }
    @GetMapping(value = "/count-of-exceptions-by-customer-wise")
    public List<CustomerWiseExceptionSummaryReport> getCountOfExceptionsByCustomerWise(){
        return exceptionsSummaryReportApiService.getCountOfExceptionsByCustomerWise();
    }

}
