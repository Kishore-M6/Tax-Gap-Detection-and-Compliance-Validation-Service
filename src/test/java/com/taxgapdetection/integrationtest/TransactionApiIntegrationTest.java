package com.taxgapdetection.integrationtest;

import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.helper.ValidationStatus;
import com.taxgapdetection.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TransactionApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository repository;

    @Test
    void testShouldUploadTransactionsSuccessfully() throws Exception {

        String json = """
                [
                  {
                    "transactionId": "TXN1001",
                    "date": "2025-02-01",
                    "customerId": "CUST101",
                    "amount": 10000,
                    "taxRate": 0.18,
                    "reportedTax": 1800,
                    "transactionType": "SALE"
                  }
                ]
                """;

        mockMvc.perform(post("/api/transactions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        Transaction saved =
                repository.findByTransactionId("TXN1001").orElseThrow();

        assertEquals(ValidationStatus.SUCCESS,
                saved.getValidationStatus());
    }

    @Test
    void testShouldMarkTransactionFailureWhenValidationFails() throws Exception {

        String json = """
                [
                    {
                        "transactionId": "TXN1006",
                        "date": "2025-02-05",
                        "customerId": "CUST103",
                        "amount": -1000,
                        "taxRate": 0.18,
                        "reportedTax": 180,
                        "transactionType": "EXPENSE"
                    }
                ]
                """;

        mockMvc.perform(post("/api/transactions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        Transaction saved =
                repository.findByTransactionId("TXN1006").orElseThrow();

        assertEquals(ValidationStatus.FAILURE,
                saved.getValidationStatus());
    }

    @Test
    void testShouldReturnBadRequest_whenInputIsEmpty() throws Exception {
        String json = """
                [
                ]
                """;
        mockMvc.perform(post("/api/transactions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testShouldThrowExceptionForDuplicateTransaction() throws Exception {

        String json = """
                [
                  {
                    "transactionId": "TXN1019",
                    "amount": 100
                  }
                ]
                """;
        // First call
        mockMvc.perform(post("/api/transactions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        // Second call → Duplicate Transaction
        mockMvc.perform(post("/api/transactions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testShouldProcessBatchOfTransactions() throws Exception {

        String json = """
                [
                     {
                     "transactionId": "TXN1001",
                     "date": "2025-02-01",
                     "customerId": "CUST101",
                     "amount": 10000,
                     "taxRate": 0.18,
                     "reportedTax": 1800,
                     "transactionType": "SALE"
                   },
                   {
                     "transactionId": "TXN1002",
                     "date": "2025-02-02",
                     "customerId": "CUST101",
                     "amount": 200000,
                     "taxRate": 0.18,
                     "reportedTax": 36000,
                     "transactionType": "SALE"
                      },
                      {
                     "transactionId": "TXN1003",
                     "date": "2025-02-03",
                     "customerId": "CUST102",
                     "amount": 5000,
                     "taxRate": 0.18,
                     "reportedTax": 500,
                     "transactionType": "SALE"
                     },
                      {
                     "transactionId": "TXN1004",
                     "date": "2025-02-04",
                     "customerId": "CUST101",
                     "amount": 15000,
                     "taxRate": 0.18,
                     "reportedTax": 2700,
                     "transactionType": "REFUND"
                     },
                      {
                     "transactionId": "TXN1005",
                     "date": "2025-02-03",
                     "customerId": "CUST101",
                     "amount": 250000,
                     "taxRate": 0.18,
                     "reportedTax": 800,
                     "transactionType": "REFUND"
                      },
                     {
                     "transactionId": "TXN1006",
                     "date": "2025-02-05",
                     "customerId": "CUST103",
                     "amount": -1000,
                     "taxRate": 0.18,
                     "reportedTax": 180,
                     "transactionType": "EXPENSE"
                      },
                      {
                     "transactionId": "TXN1007",
                     "date": "2025-02-06",
                     "customerId": "CUST104",
                     "amount": 120000,
                     "taxRate": 0.05,
                     "reportedTax": 6000,
                     "transactionType": "SALE"
                   },
                      {
                     "transactionId": "TXN-COMP-001",
                     "date": "2025-02-10",
                     "customerId": "CUST105",
                     "amount": 10000,
                     "taxRate": 0.18,
                     "reportedTax": 1800,
                     "transactionType": "SALE"
                   },
                   {
                     "transactionId": "TXN-UNDER-001",
                     "date": "2025-02-10",
                     "customerId": "CUST106",
                     "amount": 10000,
                     "taxRate": 0.18,
                     "reportedTax": 1500,
                     "transactionType": "SALE"
                   },
                   {
                     "transactionId": "TXN-OVER-001",
                     "date": "2025-02-10",
                     "customerId": "CUST107",
                     "amount": 10000,
                     "taxRate": 0.18,
                     "reportedTax": 2200,
                     "transactionType": "SALE"
                   },
                   {
                     "transactionId": "TXN-NON-001",
                     "date": "2025-02-10",
                     "customerId": "CUST108",
                     "amount": 10000,
                     "taxRate": 0.18,
                     "reportedTax": null,
                     "transactionType": "SALE"
                   },
                   {
                     "transactionId": "TXN-BOUNDARY-COMP-001",
                     "date": "2025-02-10",
                     "customerId": "CUST108",
                     "amount": 10000,
                     "taxRate": 0.18,
                     "reportedTax": 1799,
                     "transactionType": "SALE"
                   },
                   {
                     "transactionId": "TXN-BOUNDARY-COMP-002",
                     "date": "2025-02-10",
                     "customerId": "CUST109",
                     "amount": 10000,
                     "taxRate": 0.18,
                     "reportedTax": 1801,
                     "transactionType": "SALE"
                   }
                 ]
        """;

        mockMvc.perform(post("/api/transactions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertEquals(13, repository.count());
    }
}
