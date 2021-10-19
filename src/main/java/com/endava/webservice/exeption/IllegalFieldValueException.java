package com.endava.webservice.exeption;

public class IllegalFieldValueException extends RuntimeException {
    public IllegalFieldValueException(String message) {
        super(message);
    }
}