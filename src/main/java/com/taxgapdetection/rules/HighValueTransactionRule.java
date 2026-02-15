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
public class HighValueTransactionRule implements TaxRuleHandler {


    private final ExceptionsRepository exceptionsRepo;
    private final ObjectMapper mapper;

    @Override
    public void apply(Transaction tx, TaxRule rule) {
        try {
            JsonNode jsonNode = mapper.readTree(rule.getConfigJson());
            BigDecimal threshold = new BigDecimal(jsonNode.get("threshold").asText());

            if (tx.getAmount().compareTo(threshold) > 0) {
                exceptionsRepo.save(
                        TaxExceptions.of()
                                .transactionId(tx.getTransactionId())
                                .customerId(tx.getCustomerId())
                                .ruleName(rule.getRuleName())
                                .severity(Severity.HIGH)
                                .message("High value transaction")
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
        return "HighValueRule";
    }
}
