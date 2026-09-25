package com.example.accountservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountTransferRequest(

        UUID sourceAccountId,

        UUID destinationAccountId,

        BigDecimal amount,

        String currency,

        String transferReference

) {
}
