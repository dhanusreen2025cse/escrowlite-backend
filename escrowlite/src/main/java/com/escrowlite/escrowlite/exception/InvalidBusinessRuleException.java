package com.escrowlite.escrowlite.exception;

public class InvalidBusinessRuleException extends RuntimeException {

    public InvalidBusinessRuleException(String message) {
        super(message);
    }

    public InvalidBusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}