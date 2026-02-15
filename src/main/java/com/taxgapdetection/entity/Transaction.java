package com.taxgapdetection.entity;

import com.taxgapdetection.helper.ComplianceStatus;
import com.taxgapdetection.helper.TransactionType;
import com.taxgapdetection.helper.ValidationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions",indexes = {
        @Index(name = "idx_txn_transaction_id", columnList = "transactionId",unique = true),
        @Index(name = "idx_txn_customer_id", columnList = "customerId"),
        @Index(name = "idx_txn_date", columnList = "date"),
        @Index(name = "idx_txn_validation_status", columnList = "validationStatus")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "of")
public class Transaction {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String transactionId;
    private LocalDate date;
    private String customerId;
    private BigDecimal amount;
    private BigDecimal taxRate;
    private BigDecimal reportedTax;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private ValidationStatus validationStatus;
    private String failureReasons;
    private BigDecimal expectedTax;
    private BigDecimal taxGap;
    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;
}

