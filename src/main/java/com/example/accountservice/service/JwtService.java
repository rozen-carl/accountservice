package com.example.accountservice.service;

import java.util.UUID;

public interface JwtService {

    String extractUsername(String token);

    boolean isTokenValid(String token);

    UUID extractCustomerId(String token);
}
