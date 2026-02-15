package com.taxgapdetection.unittest.service;

import com.taxgapdetection.dto.TransactionDto;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.exception.DuplicateTransactionException;
import com.taxgapdetection.helper.EventType;
import com.taxgapdetection.helper.ValidationStatus;
import com.taxgapdetection.repository.TransactionRepository;
import com.taxgapdetection.rules.RulesEngine;
import com.taxgapdetection.service.AuditLogService;
import com.taxgapdetection.service.TaxComputationService;
import com.taxgapdetection.service.TransactionApiService;
import com.taxgapdetection.validator.TransactionValidator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionApiServiceTest {
    @Mock
    private TransactionRepository repository;
    @Mock
    private TaxComputationService taxService;
    @Mock
    private RulesEngine rulesEngine;
    @Mock
    private AuditLogService auditLogService;
    @Mock
    private TransactionValidator validator;

    @InjectMocks
    private TransactionApiService transactionApiService;
    @Captor
    ArgumentCaptor<Transaction> transactionCaptor;

    private Transaction transaction;
    private TransactionDto dto;
    @BeforeEach
    void setUp() {
        dto = new TransactionDto();
        dto.setTransactionId("TXN1010");
    }


    @Test
    void testProcessBatchOfTransactionAsSuccessfully() {

        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        transactionApiService.processBatchOfTransactions(List.of(dto));
        verify(validator).validate(any(Transaction.class));
        verify(taxService).compute(any(Transaction.class));
        verify(rulesEngine).execute(any(Transaction.class));
        verify(repository).save(transactionCaptor.capture());
        verify(auditLogService).log(eq(EventType.INGESTION),eq("TXN1010"),any());

        Transaction savedTxn = transactionCaptor.getValue();

        assertEquals("TXN1010", savedTxn.getTransactionId());
        assertEquals(ValidationStatus.SUCCESS, savedTxn.getValidationStatus());
    }
    @Test
    void tetFailureCase_WhenValidationFails() {

        doThrow(new RuntimeException())
                .when(validator).validate(any());

        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        transactionApiService.processBatchOfTransactions(List.of(dto));

        verify(taxService, never()).compute(any(Transaction.class));
        verify(rulesEngine, never()).execute(any(Transaction.class));
        verify(repository).save(transactionCaptor.capture());

        Transaction savedTxn = transactionCaptor.getValue();

        assertEquals(ValidationStatus.FAILURE,
               savedTxn.getValidationStatus() );

    }
    @Test
    void testThrowDuplicateExceptionForSameTransaction() {

        when(repository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateTransactionException.class,
                () -> transactionApiService.processBatchOfTransactions(List.of(dto)));
    }
    @Test
    void testProcessAllTransactionsInBatch() {

        TransactionDto dto2 = new TransactionDto();
        dto2.setTransactionId("TXN1012");

        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        transactionApiService.processBatchOfTransactions(List.of(dto, dto2));

        verify(repository, times(2)).save(any(Transaction.class));
    }

}