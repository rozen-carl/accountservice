package com.example.accountservice.client;

import com.example.accountservice.dto.CustomerResponse;

import java.util.UUID;

public interface CustomerClient {

    CustomerResponse getCustomer(UUID customerId);
}
