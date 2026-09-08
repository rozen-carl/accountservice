package com.example.accountservice.repository;

import com.example.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository
        extends JpaRepository<Account, UUID> {

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByCustomerIdAndAccountTypeAndStatus(
            UUID customerId,
            Account.AccountType accountType,
            Account.AccountStatus status
    );

    Optional<Account> findByAccountNumber(String accountNumber);
}
