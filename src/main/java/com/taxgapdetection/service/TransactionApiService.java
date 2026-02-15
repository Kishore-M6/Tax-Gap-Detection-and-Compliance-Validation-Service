package com.taxgapdetection.service;

import com.taxgapdetection.dto.TransactionDto;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.exception.DuplicateTransactionException;
import com.taxgapdetection.helper.EventType;
import com.taxgapdetection.helper.ValidationStatus;
import com.taxgapdetection.mapper.DtoToEntityMapper;
import com.taxgapdetection.repository.TransactionRepository;
import com.taxgapdetection.response.BatchTransactionResponse;
import com.taxgapdetection.response.TransactionResponse;
import com.taxgapdetection.rules.RulesEngine;
import com.taxgapdetection.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionApiService {

    private final TransactionRepository repository;
    private final TaxComputationService taxService;
    private final RulesEngine rulesEngine;
    private final AuditLogService auditLogService;
    private final TransactionValidator validator;
    private final DtoToEntityMapper mapper;

    public BatchTransactionResponse processBatchOfTransactions(List<TransactionDto> transactionDtos){

        List<TransactionResponse> results=new ArrayList<>();
        for (TransactionDto dto: transactionDtos){
            Transaction transaction =mapper.map(dto);
            try{

                validator.validate(transaction);
                taxService.compute(transaction);
                rulesEngine.execute(transaction);
                transaction.setValidationStatus(ValidationStatus.SUCCESS);
            }catch (RuntimeException e){
                transaction.setValidationStatus(ValidationStatus.FAILURE);

                transaction.setFailureReasons(e.getMessage());
            }
            try {
               Transaction txn = repository.save(transaction);
               TransactionResponse res =TransactionResponse.of().transactionId(txn.getTransactionId())
                       .failureReasons(txn.getFailureReasons())
                       .validationStatus(txn.getValidationStatus()).build();
               results.add(res);

            } catch (DataIntegrityViolationException ex) {

                throw new DuplicateTransactionException(
                        "Transaction already exists: "
                                + transaction.getTransactionId());
            }


            auditLogService.log(EventType.INGESTION, transaction.getTransactionId(),dto);

        }
        return BatchTransactionResponse.of().message("Batch Of Transactions Processed Successfully").response(results).build();

    }
}
