package com.example.accountservice.controller;

import com.example.accountservice.dto.AccountResponse;
import com.example.accountservice.dto.OpenAccountRequest;
import com.example.accountservice.dto.OpenAccountResponse;
import com.example.accountservice.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<OpenAccountResponse> openAccount(
            @Valid @RequestBody OpenAccountRequest request,
            HttpServletRequest httpRequest ) throws AccessDeniedException {

        UUID customerId =
                (UUID) httpRequest.getAttribute("customerId");

        if (customerId == null) {
            throw new AccessDeniedException(
                    "Customer identity is unavailable");
        }

        OpenAccountResponse response =
                accountService.openAccount(request, customerId);

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

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable UUID accountId) {

        AccountResponse response =
                accountService.getAccountById(accountId);

        return ResponseEntity.ok(response);
    }
}
