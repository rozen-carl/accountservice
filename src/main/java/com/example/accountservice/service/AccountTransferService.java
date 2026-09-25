package com.example.accountservice.service;

import com.example.accountservice.dto.AccountTransferRequest;

public interface AccountTransferService {

    void transfer(AccountTransferRequest request);
}
