package com.taxgapdetection.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tax_rules",indexes = {
        @Index(name = "idx_rule_name", columnList = "ruleName"),
        @Index(name = "idx_rule_enabled", columnList = "enabled")
})
@NoArgsConstructor
public class TaxRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ruleName;
    private Boolean enabled;
    @Column(columnDefinition = "TEXT")
    private String configJson;

    public TaxRule(String ruleName, Boolean enabled, String configJson) {
        this.ruleName = ruleName;
        this.enabled = enabled;
        this.configJson = configJson;
    }
}
