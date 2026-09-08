CREATE TABLE savings_accounts
(
    account_id      BINARY(16) NOT NULL,

    interest_rate   DECIMAL(5, 2)  NOT NULL,

    minimum_balance DECIMAL(19, 4) NOT NULL DEFAULT 0.0000,

    created_date    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (account_id),

    CONSTRAINT fk_savings_accounts_account
        FOREIGN KEY (account_id)
            REFERENCES accounts (id)
            ON DELETE CASCADE
);