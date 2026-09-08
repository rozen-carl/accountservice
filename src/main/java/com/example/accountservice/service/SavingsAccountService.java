package com.example.accountservice.service;

import com.example.accountservice.dto.OpenSavingsAccountRequest;
import com.example.accountservice.dto.OpenSavingsAccountResponse;

public interface SavingsAccountService {

    OpenSavingsAccountResponse openAccount(
            OpenSavingsAccountRequest request
    );
}
