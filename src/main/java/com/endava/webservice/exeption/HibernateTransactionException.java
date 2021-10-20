package com.endava.webservice.exeption;

public class HibernateTransactionException extends RuntimeException {
    public HibernateTransactionException(String message) {
        super(message);
    }
}
