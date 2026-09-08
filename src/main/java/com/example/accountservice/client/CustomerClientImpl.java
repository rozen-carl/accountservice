package com.example.accountservice.client;

import com.example.accountservice.dto.CustomerResponse;
import com.example.accountservice.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerClientImpl implements CustomerClient {

    private final RestClient customerRestClient;

    @Override
    public CustomerResponse getCustomer(UUID customerId) {

        return customerRestClient
                .get()
                .uri(
                        "/api/v1/customers/{customerId}",
                        customerId
                )
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        (request, response) -> {
                            throw new CustomerNotFoundException(
                                    "Customer not found: " + customerId
                            );
                        }
                )
                .body(CustomerResponse.class);
    }
}
