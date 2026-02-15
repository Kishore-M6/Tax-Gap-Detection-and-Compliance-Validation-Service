package com.taxgapdetection.response;

import com.taxgapdetection.helper.ComplianceStatus;
import com.taxgapdetection.helper.TransactionType;
import com.taxgapdetection.helper.ValidationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of")
public class BatchTransactionResponse {
    private String message;
   private List<TransactionResponse> response;
}
