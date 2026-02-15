package com.taxgapdetection.rules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxgapdetection.entity.TaxRule;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.entity.TaxExceptions;
import com.taxgapdetection.handler.TaxRuleHandler;
import com.taxgapdetection.helper.Severity;
import com.taxgapdetection.repository.ExceptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GSTSlabViolationRule implements TaxRuleHandler {

    private final ExceptionsRepository exceptionsRepo;
    private final ObjectMapper mapper;
    @Override
    public void apply(Transaction tx, TaxRule rule) {

        try {
            JsonNode jsonNode =mapper.readTree(rule.getConfigJson());
            BigDecimal threshold = jsonNode.get("amountThreshold").decimalValue();
            BigDecimal requiredRate = jsonNode.get("requiredTaxRate").decimalValue();

            if(tx.getAmount().compareTo(threshold) > 0 &&
                    tx.getTaxRate().compareTo(requiredRate) < 0){
                exceptionsRepo.save(
                        TaxExceptions.of()
                                .transactionId(tx.getTransactionId())
                                .customerId(tx.getCustomerId())
                                .ruleName("GstSlabViolationRule")
                                .severity(Severity.MEDIUM)
                                .message("GST slab violation")
                                .timestamp(LocalDate.now())
                                .build()
                );
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid rule JSON");
        }
    }

    @Override
    public String getRuleName() {
        return "GstSlabViolationRule";
    }

}
