package com.taxgapdetection.unittest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxgapdetection.controller.TransactionApiController;
import com.taxgapdetection.dto.TransactionDto;
import com.taxgapdetection.helper.TransactionType;
import com.taxgapdetection.response.BatchTransactionResponse;
import com.taxgapdetection.service.TransactionApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionApiController.class)
class TransactionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionApiService transactionApiService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testUploadTransactions() throws Exception {
        List<TransactionDto> dtos = List.of(buildTransactionDto());

        BatchTransactionResponse response =
                BatchTransactionResponse.of()
                        .message("Batch Of Transactions Processed Successfully")
                        .build();

        when(transactionApiService.processBatchOfTransactions(anyList()))
                .thenReturn(response);


        mockMvc.perform(
                        post("/api/transactions/upload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dtos))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Batch Of Transactions Processed Successfully"));
        //Verifying transactionApiService called once
        verify(transactionApiService,times(1)).processBatchOfTransactions(anyList());
    }
    @Test
    void testShouldReturnBadRequest_whenListIsEmpty() throws Exception {


        mockMvc.perform(
                        post("/api/transactions/upload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]")
                )
                .andExpect(status().isBadRequest());

        verify(transactionApiService, never())
                .processBatchOfTransactions(anyList());
    }

    private TransactionDto buildTransactionDto() {
        TransactionDto dto=new TransactionDto();
        dto.setTransactionId("TXN-1008");
        dto.setCustomerId("CUST110");
        dto.setAmount(new BigDecimal("10000"));
        dto.setTaxRate(new BigDecimal("0.18"));
        dto.setReportedTax(new BigDecimal("180"));
        dto.setDate(LocalDate.now());
        dto.setTransactionType(TransactionType.EXPENSE);
        return dto;
    }
}