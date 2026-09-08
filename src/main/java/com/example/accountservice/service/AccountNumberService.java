package com.example.accountservice.service;

import com.example.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountNumberService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator generator;

    public String generateUniqueAccountNumber() {

        for (int i = 0; i < 10; i++) {

            String accountNumber =
                    generator.generate();

            if (!accountRepository
                    .existsByAccountNumber(accountNumber)) {

                return accountNumber;
            }
        }

        throw new IllegalStateException(
                "Unable to generate unique account number"
        );
    }
}
