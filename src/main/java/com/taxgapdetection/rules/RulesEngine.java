package com.taxgapdetection.rules;

import com.taxgapdetection.entity.TaxRule;
import com.taxgapdetection.entity.Transaction;
import com.taxgapdetection.handler.TaxRuleHandler;
import com.taxgapdetection.helper.EventType;
import com.taxgapdetection.repository.TaxRuleRepository;
import com.taxgapdetection.service.AuditLogService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RulesEngine {
    @Autowired
    private  TaxRuleRepository taxRuleRepo;
    @Autowired
    private  AuditLogService auditLogService;
    private  Map<String, TaxRuleHandler> handlersMap;

    public RulesEngine(List<TaxRuleHandler> handlers){
        handlersMap =
                handlers.stream()
                        .collect(Collectors.toMap(
                                TaxRuleHandler::getRuleName,
                                Function.identity()
                        ));
    }

    public void execute(Transaction tx){

        List<TaxRule> rules =
                taxRuleRepo.findByEnabledTrue();

        for(TaxRule rule : rules){

            TaxRuleHandler handler =
                    handlersMap.get(rule.getRuleName());

            if(handler != null){
                handler.apply(tx, rule);
            }
            auditLogService.log(EventType.RULE_EXECUTION,tx.getTransactionId(),tx);

        }
    }
}
