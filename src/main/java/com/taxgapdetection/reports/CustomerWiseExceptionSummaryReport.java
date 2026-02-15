package com.taxgapdetection.reports;

public interface CustomerWiseExceptionSummaryReport {

    String getCustomerId();
    Long getExceptionCount();
}
