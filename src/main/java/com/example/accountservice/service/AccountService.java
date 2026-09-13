package com.example.accountservice.service;

import com.example.accountservice.dto.AccountResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    List<AccountResponse> getAccountsByCustomerId(UUID customerId);
}
