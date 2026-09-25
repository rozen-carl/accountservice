package com.example.accountservice.exception;

public class InvalidTransferAmountException extends RuntimeException {

    public InvalidTransferAmountException(String message) {
        super(message);
    }
}