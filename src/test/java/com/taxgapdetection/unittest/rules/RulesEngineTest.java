package com.taxgapdetection.unittest.rules;

import com.taxgapdetection.entity.TaxRule;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.handler.TaxRuleHandler;
import com.taxgapdetection.helper.EventType;
import com.taxgapdetection.helper.TransactionType;
import com.taxgapdetection.repository.TaxRuleRepository;
import com.taxgapdetection.rules.RulesEngine;
import com.taxgapdetection.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RulesEngineTest {

    @Mock
    AuditLogService auditLogService;
    @Mock
    TaxRuleRepository taxRuleRepo;
    @Mock
    TaxRuleHandler handler;


    RulesEngine rulesEngine;

    Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setTransactionId("TXN1015");
        transaction.setCustomerId("CUST105");
        transaction.setDate(LocalDate.of(2026, 02, 4));
        transaction.setAmount(new BigDecimal("10000"));
        transaction.setTaxRate(new BigDecimal("0.18"));
        transaction.setReportedTax(new BigDecimal("1800"));
        transaction.setTransactionType(TransactionType.SALE);

        when(handler.getRuleName()).thenReturn("HighValueRule");

        rulesEngine =
                new RulesEngine(List.of(handler));

        ReflectionTestUtils.setField(
                rulesEngine,
                "taxRuleRepo",
                taxRuleRepo);

        ReflectionTestUtils.setField(
                rulesEngine,
                "auditLogService",
                auditLogService);
    }

    @Test
    void testExecuteWithSuccessfulCase() {

        TaxRule rule = new TaxRule();
        rule.setRuleName("HighValueRule");

        when(taxRuleRepo.findByEnabledTrue())
                .thenReturn(List.of(rule));

        rulesEngine.execute(transaction);

        verify(handler)
                .apply(eq(transaction), eq(rule));

        verify(auditLogService, times(1))
                .log(eq(EventType.RULE_EXECUTION),
                        eq("TXN1015"),
                        eq(transaction));
    }
}