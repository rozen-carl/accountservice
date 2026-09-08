package com.example.accountservice.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {

    private final SecureRandom random = new SecureRandom();

    public String generate() {

        long number = 1000000000L
                + random.nextLong(9000000000L);

        return String.valueOf(number);
    }
}
