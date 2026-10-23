package com.example.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_accounts_account_number",
                        columnNames = "account_number"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID customerId;

    @Column
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column
    private AccountStatus status;

    @Column
    private String currency;

    @Column
    private BigDecimal balance;

    public enum AccountType {

        SAVINGS,
        CURRENT
    }

    public enum AccountStatus {

        PENDING,
        ACTIVE,
        SUSPENDED,
        CLOSED
    }
}
