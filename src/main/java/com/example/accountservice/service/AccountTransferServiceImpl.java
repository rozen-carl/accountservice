package com.example.accountservice.service;

import com.example.accountservice.dto.AccountTransferRequest;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.AccountTransaction;
import com.example.accountservice.exception.AccountNotFoundException;
import com.example.accountservice.exception.InsufficientFundsException;
import com.example.accountservice.exception.InvalidTransferAmountException;
import com.example.accountservice.exception.InvalidTransferException;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountTransferServiceImpl
        implements AccountTransferService {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionRepository;

    @Override
    @Transactional
    public void transfer(AccountTransferRequest request) {

        validateRequest(request);

        Account sourceAccount =
                accountRepository.findById(
                        request.sourceAccountId()
                ).orElseThrow(() ->
                        new AccountNotFoundException(
                                "Source account not found"
                        )
                );

        Account destinationAccount =
                accountRepository.findById(
                        request.destinationAccountId()
                ).orElseThrow(() ->
                        new AccountNotFoundException(
                                "Destination account not found"
                        )
                );

        validateAccounts(
                sourceAccount,
                destinationAccount,
                request
        );

        BigDecimal sourceBalanceBefore =
                sourceAccount.getBalance();

        BigDecimal destinationBalanceBefore =
                destinationAccount.getBalance();

        BigDecimal sourceBalanceAfter =
                sourceBalanceBefore.subtract(
                        request.amount()
                );

        BigDecimal destinationBalanceAfter =
                destinationBalanceBefore.add(
                        request.amount()
                );

        // Update balances

        sourceAccount.setBalance(
                sourceBalanceAfter
        );

        destinationAccount.setBalance(
                destinationBalanceAfter
        );

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Create ledger entries

        createTransferTransaction(
                sourceAccount,
                request.amount(),
                request.currency(),
                sourceBalanceBefore,
                sourceBalanceAfter,
                generateAccountTransactionReference(),
                "Transfer to own account"
        );

        createTransferTransaction(
                destinationAccount,
                request.amount(),
                request.currency(),
                destinationBalanceBefore,
                destinationBalanceAfter,
                generateAccountTransactionReference(),
                "Transfer from own account"
        );
    }

    private void createTransferTransaction(
            Account account,
            BigDecimal amount,
            String currency,
            BigDecimal balanceBefore,
            BigDecimal balanceAfter,
            String transactionReference,
            String description) {

        AccountTransaction transaction =
                AccountTransaction.builder()
                        .id(UUID.randomUUID())
                        .account(account)
                        .transactionReference(transactionReference)
                        .transactionType(
                                AccountTransaction.TransactionType.TRANSFER
                        )
                        .amount(amount)
                        .currency(currency)
                        .balanceBefore(balanceBefore)
                        .balanceAfter(balanceAfter)
                        .description(description)
                        .transactionDate(Instant.now())
                        .build();

        transactionRepository.save(transaction);
    }

    private void validateRequest(
            AccountTransferRequest request) {

        if (request.amount() == null ||
                request.amount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidTransferAmountException(
                    "Transfer amount must be greater than zero"
            );
        }

        if (request.sourceAccountId()
                .equals(request.destinationAccountId())) {

            throw new InvalidTransferException(
                    "Source and destination accounts must be different"
            );
        }
    }

    private void validateAccounts(
            Account sourceAccount,
            Account destinationAccount,
            AccountTransferRequest request) {

        if (!sourceAccount.getStatus()
                .equals(Account.AccountStatus.ACTIVE)) {

            throw new InvalidTransferException(
                    "Source account is not active"
            );
        }

        if (!destinationAccount.getStatus()
                .equals(Account.AccountStatus.ACTIVE)) {

            throw new InvalidTransferException(
                    "Destination account is not active"
            );
        }

        if (sourceAccount.getBalance()
                .compareTo(request.amount()) < 0) {

            throw new InsufficientFundsException(
                    "Insufficient funds"
            );
        }

        if (!sourceAccount.getCurrency()
                .equals(request.currency())) {

            throw new InvalidTransferException(
                    "Source account currency does not match"
            );
        }

        if (!destinationAccount.getCurrency()
                .equals(request.currency())) {

            throw new InvalidTransferException(
                    "Destination account currency does not match"
            );
        }
    }

    private String generateAccountTransactionReference() {

        return "AT-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 20)
                        .toUpperCase();
    }
}