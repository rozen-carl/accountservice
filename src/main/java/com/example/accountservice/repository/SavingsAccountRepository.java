package com.example.accountservice.repository;

import com.example.accountservice.entity.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SavingsAccountRepository  extends JpaRepository<SavingsAccount, UUID> {
}
