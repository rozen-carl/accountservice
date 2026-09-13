package com.example.accountservice.config;

import com.example.accountservice.security.BearerTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final BearerTokenInterceptor bearerTokenInterceptor;

    @Bean
    public RestClient customerRestClient(
            @Value("${services.customer.url}")
            String customerServiceUrl) {

        return RestClient.builder()
                .baseUrl(customerServiceUrl)
                .requestInterceptor(bearerTokenInterceptor)
                .build();
    }
}
