package com.example.accountservice.controller;

import com.example.accountservice.dto.AccountTransferRequest;
import com.example.accountservice.service.AccountTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/accounts")
@RequiredArgsConstructor
public class InternalAccountController {

    private final AccountTransferService accountTransferService;

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @Valid @RequestBody AccountTransferRequest request) {

        accountTransferService.transfer(request);

        return ResponseEntity.ok().build();
    }
}