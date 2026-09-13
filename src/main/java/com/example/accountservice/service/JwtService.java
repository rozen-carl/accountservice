package com.example.accountservice.service;

public interface JwtService {

    String extractUsername(String token);

    boolean isTokenValid(String token);
}
