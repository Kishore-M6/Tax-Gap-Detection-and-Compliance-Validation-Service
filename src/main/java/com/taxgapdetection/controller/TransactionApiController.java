package com.taxgapdetection.controller;

import com.taxgapdetection.dto.TransactionDto;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.service.TransactionApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionApiController {

    private final TransactionApiService service;

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadTransactions(@RequestBody List<TransactionDto> transactionDtos){

        if(transactionDtos == null || transactionDtos.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body("Transaction list cannot be empty");
        }

        return ResponseEntity.ok(service.processBatchOfTransactions(transactionDtos));
    }


}
