package com.example.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_transaction_reference",
                        columnNames = "transaction_reference"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransaction {

    @Id
    @Column(
            columnDefinition = "BINARY(16)",
            nullable = false
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false
    )
    private Account account;

    @Column
    private String transactionReference;

    @Enumerated(EnumType.STRING)
    @Column
    private TransactionType transactionType;

    @Column
    private BigDecimal amount;

    @Column
    private String currency;

    @Column
    private BigDecimal balanceBefore;

    @Column
    private BigDecimal balanceAfter;

    @Column
    private String description;

    @Column
    private Instant transactionDate;

    public enum TransactionType {

        INITIAL_DEPOSIT,
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER_IN,
        TRANSFER_OUT,
        INTEREST
    }
}
