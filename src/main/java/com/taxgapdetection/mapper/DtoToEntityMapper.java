package com.taxgapdetection.mapper;

import com.taxgapdetection.dto.TransactionDto;
import com.taxgapdetection.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class DtoToEntityMapper {
    public static Transaction map(TransactionDto dto){
        return Transaction.of()
                .transactionId(dto.getTransactionId())
                .date(dto.getDate())
                .customerId(dto.getCustomerId())
                .amount(dto.getAmount())
                .taxRate(dto.getTaxRate())
                .reportedTax(dto.getReportedTax())
                .transactionType(dto.getTransactionType())
                .build();
    }
}
