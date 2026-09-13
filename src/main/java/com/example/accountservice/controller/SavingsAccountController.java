package com.example.accountservice.controller;

import com.example.accountservice.dto.AccountResponse;
import com.example.accountservice.dto.OpenSavingsAccountRequest;
import com.example.accountservice.dto.OpenSavingsAccountResponse;
import com.example.accountservice.service.AccountService;
import com.example.accountservice.service.SavingsAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts/savings")
@RequiredArgsConstructor
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<OpenSavingsAccountResponse> openAccount(
            @Valid @RequestBody OpenSavingsAccountRequest request) {

        OpenSavingsAccountResponse response =
                savingsAccountService.openAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByCustomerId(
            @PathVariable UUID customerId) {

        List<AccountResponse> accounts =
                accountService.getAccountsByCustomerId(customerId);

        return ResponseEntity.ok(accounts);
    }
}
