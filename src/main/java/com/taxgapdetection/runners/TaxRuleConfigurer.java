package com.taxgapdetection.runners;

import com.taxgapdetection.entity.TaxRule;
import com.taxgapdetection.repository.TaxRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaxRuleConfigurer implements CommandLineRunner {
   private final TaxRuleRepository taxRuleRepo;
   private Logger logger= LogManager.getLogger(TaxRuleConfigurer.class);
    @Override
    public void run(String... args) throws Exception {
        if(taxRuleRepo.count() > 0){
            return;
        }

        taxRuleRepo.save(new TaxRule("HighValueRule", true, "{\"threshold\":100000}"
        ));

        taxRuleRepo.save(new TaxRule("RefundValidationRule", true, "{}"
        ));

        taxRuleRepo.save(new TaxRule("GstSlabViolationRule", true, "{\"amountThreshold\":100000,\"requiredTaxRate\":0.18}"
        ));
        logger.info("Tax rules added successfully into DB");

    }
}
