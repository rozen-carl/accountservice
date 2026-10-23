package com.example.accountservice.service;

import com.example.accountservice.dto.AccountResponse;
import com.example.accountservice.dto.OpenAccountRequest;
import com.example.accountservice.dto.OpenAccountResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    List<AccountResponse> getAccountsByCustomerId(UUID customerId);

    OpenAccountResponse openAccount(OpenAccountRequest request, UUID customerId);
}
