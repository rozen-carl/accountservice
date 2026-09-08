package com.example.accountservice.dto;

import com.example.accountservice.entity.Account;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OpenSavingsAccountResponse(

        UUID accountId,

        String accountNumber,

        Account.AccountType accountType,

        Account.AccountStatus status,

        String currency,

        BigDecimal balance,

        BigDecimal interestRate,

        Instant createdDate
) {
}
