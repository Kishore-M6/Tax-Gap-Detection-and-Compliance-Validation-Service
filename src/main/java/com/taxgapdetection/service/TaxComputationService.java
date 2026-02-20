package com.taxgapdetection.service;

import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.helper.ComplianceStatus;
import com.taxgapdetection.helper.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TaxComputationService {
    private final  AuditLogService auditLogService;
    public void compute(Transaction tx){
        if(tx.getReportedTax() == null){
            tx.setComplianceStatus(ComplianceStatus.NON_COMPLIANT);
            return;
        }

        BigDecimal expected =
                tx.getAmount().multiply(tx.getTaxRate());

        BigDecimal gap =
                expected.subtract(tx.getReportedTax());

        tx.setExpectedTax(expected);
        tx.setTaxGap(gap);

        if(gap.abs().compareTo(BigDecimal.ONE) <= 0){
            tx.setComplianceStatus(ComplianceStatus.COMPLIANT);
        }
        else if(gap.compareTo(BigDecimal.ONE) > 0){
            tx.setComplianceStatus(ComplianceStatus.UNDERPAID);
        }
        else{
            tx.setComplianceStatus(ComplianceStatus.OVERPAID);
        }

        auditLogService.log(EventType.TAX_COMPUTATION, tx.getTransactionId(),tx);

    }
}
