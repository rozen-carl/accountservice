package com.example.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    private BigDecimal interestRate;

    @Column
    private BigDecimal minimumBalance;
}
