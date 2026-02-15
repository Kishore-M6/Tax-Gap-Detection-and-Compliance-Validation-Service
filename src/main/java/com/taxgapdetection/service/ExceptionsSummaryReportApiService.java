package com.taxgapdetection.service;

import com.taxgapdetection.reports.CustomerWiseExceptionSummaryReport;
import com.taxgapdetection.reports.ExceptionSummaryReport;
import com.taxgapdetection.reports.ExceptionSummaryReportBySeverity;
import com.taxgapdetection.repository.ExceptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExceptionsSummaryReportApiService {
    private final ExceptionsRepository exceptionsRepo;

    public List<ExceptionSummaryReport> getTotalExceptions(){
      return   exceptionsRepo.getTotalExceptionsSummaryReport();
    }

    public List<ExceptionSummaryReportBySeverity> getCountOfExceptionsBySeverity(){
        return exceptionsRepo.getExceptionSummaryReportBySeverity();
    }

    public List<CustomerWiseExceptionSummaryReport> getCountOfExceptionsByCustomerWise(){
        return exceptionsRepo.getCustomerWiseExceptionSummaryReport();
    }
}
