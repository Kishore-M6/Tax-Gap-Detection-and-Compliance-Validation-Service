package com.taxgapdetection.rules;

import com.taxgapdetection.entity.TaxRule;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.entity.TaxExceptions;
import com.taxgapdetection.handler.TaxRuleHandler;
import com.taxgapdetection.helper.Severity;
import com.taxgapdetection.helper.TransactionType;
import com.taxgapdetection.repository.ExceptionsRepository;
import com.taxgapdetection.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RefundValidationRule implements TaxRuleHandler {
    private final TransactionRepository repo;
    private final ExceptionsRepository exceptionsRepo;

    @Override
    public void apply(Transaction tx, TaxRule rule) {
        if (tx.getTransactionType() != TransactionType.REFUND)
            return;
        Transaction sale =
                repo.findTopByCustomerIdAndTransactionTypeOrderByDateDesc(tx.getCustomerId(), TransactionType.SALE)
                        .orElse(null);
        if (sale == null)
            return;
        if (tx.getAmount().compareTo(sale.getAmount()) > 0) {
            exceptionsRepo.save(
                    TaxExceptions.of()
                            .transactionId(tx.getTransactionId())
                            .customerId(tx.getCustomerId())
                            .ruleName("RefundValidationRule")
                            .severity(Severity.HIGH)
                            .message("Refund exceeds original sale amount")
                            .timestamp(LocalDate.now())
                            .build()
            );
        }
    }

    @Override
    public String getRuleName() {
        return "RefundValidationRule";
    }
}
