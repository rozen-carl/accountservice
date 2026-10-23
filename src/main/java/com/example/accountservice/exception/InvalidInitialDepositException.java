package com.example.accountservice.exception;

public class InvalidInitialDepositException extends RuntimeException {

    public InvalidInitialDepositException(String message) {
        super(message);
    }
}