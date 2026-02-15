package com.taxgapdetection.service;

import com.taxgapdetection.entity.TaxExceptions;
import com.taxgapdetection.helper.Severity;
import com.taxgapdetection.repository.ExceptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxExceptionsApiService {
    private final ExceptionsRepository exceptionsRepo;

    public List<TaxExceptions> getAllExceptions() {
        return exceptionsRepo.findAll();
    }

    public List<TaxExceptions> filter(String customerId,
                                     Severity severity,
                                     String ruleName) {

        if (customerId != null && severity != null && ruleName != null) {
            return exceptionsRepo.findByCustomerIdAndSeverityAndRuleName(
                    customerId, severity, ruleName);
        }

        if (customerId != null) {
            return exceptionsRepo.findByCustomerId(customerId);
        }

        if (severity != null) {
            return exceptionsRepo.findBySeverity(severity);
        }

        if (ruleName != null) {
            return exceptionsRepo.findByRuleName(ruleName);
        }

        return exceptionsRepo.findAll();
    }
}
