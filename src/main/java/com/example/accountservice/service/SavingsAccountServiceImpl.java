package com.example.accountservice.service;

import com.example.accountservice.client.CustomerClient;
import com.example.accountservice.dto.CustomerResponse;
import com.example.accountservice.dto.OpenSavingsAccountRequest;
import com.example.accountservice.dto.OpenSavingsAccountResponse;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.AccountTransaction;
import com.example.accountservice.entity.SavingsAccount;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.repository.AccountTransactionRepository;
import com.example.accountservice.repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavingsAccountServiceImpl
        implements SavingsAccountService {

    private final CustomerClient customerClient;
    private final AccountRepository accountRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountTransactionRepository transactionRepository;
    private final AccountNumberService accountNumberService;

    @Transactional
    @Override
    public OpenSavingsAccountResponse openAccount(
            OpenSavingsAccountRequest request) {

        // Retrieve customer from Customer Service
        CustomerResponse customer =
                customerClient.getCustomer(
                        request.customerId()
                );

        // Make sure customer is active
        if (!"ACTIVE".equals(customer.status())) {
            //TODO: later
//            throw new BusinessException(
//                    "Customer is not active"
//            );
        }

        // Check if customer already has savings account
        boolean exists =
                accountRepository
                        .existsByCustomerIdAndAccountTypeAndStatus(
                                request.customerId(),
                                Account.AccountType.SAVINGS,
                                Account.AccountStatus.ACTIVE
                        );

        if (exists) {
            //TODO: Later
//            throw new BusinessException(
//                    "Customer already has an active savings account"
//            );
        }

        UUID accountId = UUID.randomUUID();

        String accountNumber =
                accountNumberService
                        .generateUniqueAccountNumber();

        BigDecimal initialDeposit =
                request.initialDeposit();

        Account account = Account.builder()
                .id(accountId)
                .customerId(customer.id())
                .accountNumber(accountNumber)
                .accountType(Account.AccountType.SAVINGS)
                .status(Account.AccountStatus.ACTIVE)
                .currency(request.currency())
                .balance(initialDeposit)
                .availableBalance(initialDeposit)
                .build();

        accountRepository.save(account);

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

        createInitialDepositTransaction(
                account,
                initialDeposit,
                request.currency()
        );

        return new OpenSavingsAccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getStatus(),
                account.getCurrency(),
                account.getBalance(),
                savingsAccount.getInterestRate(),
                account.getCreatedDate()
        );
    }

    private void validateInitialDeposit(
            BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) < 0) {

            //TODO later
//            throw new BusinessException(
//                    "Initial deposit cannot be negative"
//            );
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
                        .balanceBefore(BigDecimal.ZERO)
                        .balanceAfter(amount)
                        .description(
                                "Initial savings account deposit"
                        )
                        .transactionDate(Instant.now())
                        .build();

        transactionRepository.save(transaction);
    }
}
