package com.example.accountservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

public record OpenSavingsAccountRequest(

        @NotNull
        UUID customerId,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal initialDeposit,

        @NotBlank
        @Pattern(regexp = "GBP|EUR|USD")
        String currency
) {
}