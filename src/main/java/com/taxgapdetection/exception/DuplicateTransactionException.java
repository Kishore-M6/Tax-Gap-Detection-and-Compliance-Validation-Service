package com.taxgapdetection.exception;

public class DuplicateTransactionException extends RuntimeException {
    public DuplicateTransactionException(String msg) {
        super(msg);
    }
}
