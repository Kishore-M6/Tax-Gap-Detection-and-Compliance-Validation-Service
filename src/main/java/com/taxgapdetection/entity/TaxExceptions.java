package com.taxgapdetection.entity;


import com.taxgapdetection.helper.Severity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "of")
@Table(name = "exceptions",indexes = {
        @Index(name = "idx_exc_transaction_id", columnList = "transactionId"),
        @Index(name = "idx_exc_customer_id", columnList = "customerId"),
        @Index(name = "idx_exc_severity", columnList = "severity"),
        @Index(name = "idx_exc_rule", columnList = "ruleName")
})
public class TaxExceptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String customerId;
    private String ruleName;
    @Enumerated(EnumType.STRING)
    private Severity severity;
    private String message;
    private LocalDate timestamp;
}
