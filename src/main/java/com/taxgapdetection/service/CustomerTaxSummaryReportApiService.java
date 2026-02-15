package com.taxgapdetection.service;

import com.taxgapdetection.reports.CustomerTaxSummaryReport;
import com.taxgapdetection.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerTaxSummaryReportApiService {
    private final TransactionRepository transactionRepo;

    public List<CustomerTaxSummaryReport> generateCustomerTaxReport(){
       return transactionRepo.getCustomerTaxSummaryReport();
    }

}
