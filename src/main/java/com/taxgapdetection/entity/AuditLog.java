package com.taxgapdetection.entity;

import com.taxgapdetection.helper.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "audit_logs",indexes = {
        @Index(name = "idx_audit_txn", columnList = "transactionId"),
        @Index(name = "idx_audit_timestamp", columnList = "timestamp")
})
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "of")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String transactionId;
    private LocalDate timestamp;
    @Column(columnDefinition = "TEXT")
    private String detailJson;
}
