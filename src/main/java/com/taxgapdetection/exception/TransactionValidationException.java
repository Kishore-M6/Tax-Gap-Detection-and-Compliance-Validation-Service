package com.taxgapdetection.exception;



import java.util.List;


public class TransactionValidationException extends RuntimeException {

    private final List<String> errors;
    public TransactionValidationException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }
    public List<String> getErrors() {
        return errors;
    }

}
