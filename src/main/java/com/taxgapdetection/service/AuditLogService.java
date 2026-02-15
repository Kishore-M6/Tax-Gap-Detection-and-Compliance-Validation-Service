package com.taxgapdetection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxgapdetection.entity.AuditLog;
import com.taxgapdetection.helper.EventType;
import com.taxgapdetection.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepo;
    private final ObjectMapper mapper;

    public void log(EventType event, String txId, Object detail){
        try {
            auditLogRepo.save(
                    AuditLog.of()
                            .eventType(event)
                            .transactionId(txId)
                            .timestamp(LocalDate.now())
                            .detailJson(mapper.writeValueAsString(detail))
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to parse JSON into String");
        }
    }

}
