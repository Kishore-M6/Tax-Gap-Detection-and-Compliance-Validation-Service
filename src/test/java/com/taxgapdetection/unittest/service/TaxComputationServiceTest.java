package com.taxgapdetection.unittest.service;

import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.helper.ComplianceStatus;
import com.taxgapdetection.helper.EventType;
import com.taxgapdetection.service.AuditLogService;
import com.taxgapdetection.service.TaxComputationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TaxComputationServiceTest {

    @Mock
    AuditLogService auditLogService;

    @InjectMocks
    private TaxComputationService taxComputationService;


    @Test
    void testComputeSuccessful() {
        Transaction txn=new Transaction();
        txn.setTransactionId("TXN1013");
        txn.setAmount(new BigDecimal(1000));
        txn.setReportedTax(new BigDecimal(1700));
        txn.setTaxRate(new BigDecimal(0.18));

        taxComputationService.compute(txn);

        assertEquals(ComplianceStatus.OVERPAID,
                txn.getComplianceStatus());

        verify(auditLogService)
                .log(eq(EventType.TAX_COMPUTATION),
                        eq("TXN1013"),
                        eq(txn));
    }

    @Test
    void testComputeWhenReportTaxWithNull(){
        Transaction txn=new Transaction();
        txn.setTransactionId("TXN1013");
        taxComputationService.compute(txn);

        assertEquals(ComplianceStatus.NON_COMPLIANT,
                txn.getComplianceStatus());
    }

}