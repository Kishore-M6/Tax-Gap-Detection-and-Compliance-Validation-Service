package com.taxgapdetection.validator;

import com.taxgapdetection.dto.TransactionDto;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.exception.TransactionValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidator {
    public void validate(Transaction txn){
        List<String> errors = new ArrayList<>();

        if (txn.getTransactionId() == null || txn.getTransactionId().isBlank()) {
            errors.add("transactionId is required");
        }
        if (txn.getCustomerId() == null || txn.getCustomerId().isBlank()) {
            errors.add("customerId is required");
        }
        if (txn.getDate() == null) {
            errors.add("date is required");
        }
        // Amount must be > 0
        if (txn.getAmount() == null ||
                txn.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            errors.add("amount must be greater than zero or can not be null");
        }
        if (txn.getTaxRate() == null ||
                txn.getTaxRate().compareTo(BigDecimal.ZERO) <= 0) {

            errors.add("taxRate must be greater than zero or can not be null");
        }
        if (txn.getReportedTax() == null ||
                txn.getReportedTax().compareTo(BigDecimal.ZERO) <= 0) {

            errors.add("reportedTax must be greater than zero or can not be null");
        }
        if (txn.getTransactionType() == null) {
            errors.add("transactionType is required");
        }

        if (!errors.isEmpty()) {
            throw new TransactionValidationException(errors);
        }

    }

}
