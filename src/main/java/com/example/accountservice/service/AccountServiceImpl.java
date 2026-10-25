package com.example.accountservice.service;

import com.example.accountservice.client.CustomerClient;
import com.example.accountservice.dto.AccountResponse;
import com.example.accountservice.dto.CustomerResponse;
import com.example.accountservice.dto.OpenAccountRequest;
import com.example.accountservice.dto.OpenAccountResponse;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.AccountTransaction;
import com.example.accountservice.entity.SavingsAccount;
import com.example.accountservice.exception.AccountAlreadyExistsException;
import com.example.accountservice.exception.AccountNotFoundException;
import com.example.accountservice.exception.CustomerNotActiveException;
import com.example.accountservice.exception.InvalidInitialDepositException;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.repository.AccountTransactionRepository;
import com.example.accountservice.repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final CustomerClient customerClient;
    private final AccountRepository accountRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountTransactionRepository transactionRepository;
    private final AccountNumberService accountNumberService;

    @Transactional
    @Override
    public OpenAccountResponse openAccount(
            OpenAccountRequest request, UUID customerId) {

        validateRequest(request);

        // Retrieve customer from Customer Service
        CustomerResponse customer =
                customerClient.getCustomer(
                        customerId
                );

        // Make sure customer is active
        if (!"ACTIVE".equals(customer.status())) {
            throw new CustomerNotActiveException(
                    "Customer is not active"
            );
        }

        // Check whether customer already has
        // an active account of this type
        boolean exists =
                accountRepository
                        .existsByCustomerIdAndAccountTypeAndStatus(
                                customerId,
                                request.accountType(),
                                Account.AccountStatus.ACTIVE
                        );

        if (exists) {
            throw new AccountAlreadyExistsException(
                    "Customer already has an active "
                            + request.accountType().name().toLowerCase()
                            + " account"
            );
        }

        String accountNumber =
                accountNumberService
                        .generateUniqueAccountNumber();

        BigDecimal initialDeposit =
                request.initialDeposit();

        Account account = Account.builder()
                .customerId(customer.id())
                .accountNumber(accountNumber)
                .accountType(request.accountType())
                .status(Account.AccountStatus.ACTIVE)
                .currency(request.currency())
                .balance(initialDeposit)
                .build();

        accountRepository.save(account);

        BigDecimal interestRate = null;

        /*
         * Savings-specific logic
         */
        if (request.accountType()
                == Account.AccountType.SAVINGS) {

            SavingsAccount savingsAccount =
                    SavingsAccount.builder()
                            .account(account)
                            .interestRate(
                                    new BigDecimal("2.50")
                            )
                            .minimumBalance(
                                    new BigDecimal("0.00")
                            )
                            .build();

            savingsAccountRepository.save(
                    savingsAccount
            );

            interestRate =
                    savingsAccount.getInterestRate();
        }

        /*
         * Create initial deposit ledger entry
         */
        createInitialDepositTransaction(
                account,
                initialDeposit,
                request.currency()
        );

        return new OpenAccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getStatus(),
                account.getCurrency(),
                account.getBalance(),
                interestRate,
                account.getCreatedDate()
        );
    }

    private void validateRequest(
            OpenAccountRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Account request must not be null"
            );
        }

        if (request.accountType() == null) {
            throw new IllegalArgumentException(
                    "Account type is required"
            );
        }

        if (request.currency() == null ||
                request.currency().isBlank()) {

            throw new IllegalArgumentException(
                    "Currency is required"
            );
        }

        if (request.currency().length() != 3) {
            throw new IllegalArgumentException(
                    "Currency must be a 3-letter currency code"
            );
        }

        validateInitialDeposit(
                request.initialDeposit()
        );
    }

    private void validateInitialDeposit(
            BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) < 0) {

            throw new InvalidInitialDepositException(
                    "Initial deposit cannot be negative"
            );
        }
    }

    private void createInitialDepositTransaction(
            Account account,
            BigDecimal amount,
            String currency) {

        AccountTransaction transaction =
                AccountTransaction.builder()
                        .id(UUID.randomUUID())
                        .account(account)
                        .transactionReference(
                                "TXN-" +
                                        UUID.randomUUID()
                                                .toString()
                                                .replace("-", "")
                                                .substring(0, 20)
                                                .toUpperCase()
                        )
                        .transactionType(
                                AccountTransaction.TransactionType.INITIAL_DEPOSIT
                        )
                        .amount(amount)
                        .currency(currency)
                        .balanceBefore(
                                BigDecimal.ZERO
                        )
                        .balanceAfter(amount)
                        .description(
                                "Initial " +
                                        account.getAccountType()
                                                .name()
                                                .toLowerCase() +
                                        " account deposit"
                        )
                        .transactionDate(Instant.now())
                        .build();

        transactionRepository.save(transaction);
    }

    @Override
    public List<AccountResponse> getAccountsByCustomerId(UUID customerId) {

        List<Account> accounts =
                accountRepository.findByCustomerId(customerId);

        return accounts.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AccountResponse getAccountById(UUID accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        return new AccountResponse(
                account.getId(),
                account.getCustomerId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getStatus(),
                account.getCurrency(),
                account.getBalance(),
                account.getCreatedDate()
        );
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getCustomerId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getStatus(),
                account.getCurrency(),
                account.getBalance(),
                account.getCreatedDate()
        );
    }
}
