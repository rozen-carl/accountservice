package com.example.accountservice.dto;

import com.example.accountservice.entity.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

public record OpenAccountRequest(
        Account.AccountType accountType,
        String currency,
        BigDecimal initialDeposit) {
}