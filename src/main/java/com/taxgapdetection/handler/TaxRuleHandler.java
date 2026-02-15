package com.taxgapdetection.handler;

import com.taxgapdetection.entity.TaxRule;
import com.taxgapdetection.entity.Transaction;

public interface TaxRuleHandler {
    String getRuleName();
    void apply(Transaction tx, TaxRule rule);
}
