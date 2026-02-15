package com.taxgapdetection.dto;

import com.taxgapdetection.helper.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionDto {
    @NotNull
    private String transactionId;
    @NotNull
    private LocalDate date;
    @NotNull
    private String customerId;
    @Positive
    @NotNull
    private BigDecimal amount;
    @NotNull
    @Positive
    private BigDecimal taxRate;
    @NotNull
    @Positive
    private BigDecimal reportedTax;
    @NotNull
    private TransactionType transactionType;

}
