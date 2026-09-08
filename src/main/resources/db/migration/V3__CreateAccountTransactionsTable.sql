CREATE TABLE account_transactions
(
    id                    BINARY(16) NOT NULL,

    account_id            BINARY(16) NOT NULL,

    transaction_reference VARCHAR(50)    NOT NULL,

    transaction_type      VARCHAR(30)    NOT NULL,

    amount                DECIMAL(19, 4) NOT NULL,

    currency              CHAR(3)        NOT NULL,

    balance_before        DECIMAL(19, 4) NOT NULL,

    balance_after         DECIMAL(19, 4) NOT NULL,

    description           VARCHAR(255),

    transaction_date      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_transaction_reference
        UNIQUE (transaction_reference),

    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id)
            REFERENCES accounts (id),

    INDEX                 idx_transactions_account_id (account_id),
    INDEX                 idx_transactions_transaction_date (transaction_date)
);