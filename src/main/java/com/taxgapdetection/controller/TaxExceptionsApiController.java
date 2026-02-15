package com.taxgapdetection.controller;

import com.taxgapdetection.entity.TaxExceptions;
import com.taxgapdetection.helper.Severity;
import com.taxgapdetection.service.TaxExceptionsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exceptions")
public class TaxExceptionsApiController {

    private final TaxExceptionsApiService taxExceptionsApiService;

    @GetMapping(value = "/getAll")
    public List<TaxExceptions> getAllExceptions() {
        return taxExceptionsApiService.getAllExceptions();
    }

    @GetMapping(value = "/filter")
    public List<TaxExceptions> filter(@RequestParam(required = false) String customerId,
                                      @RequestParam(required = false) Severity severity,
                                      @RequestParam(required = false) String ruleName) {

        return taxExceptionsApiService.filter(customerId, severity, ruleName);
    }
}
