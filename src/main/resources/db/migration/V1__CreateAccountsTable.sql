CREATE TABLE accounts
(
    id                 BINARY(16) NOT NULL,

    customer_id        BINARY(16) NOT NULL,

    account_number     VARCHAR(20)    NOT NULL,

    account_type       VARCHAR(30)    NOT NULL,

    status             VARCHAR(30)    NOT NULL,

    currency           CHAR(3)        NOT NULL DEFAULT 'GBP',

    balance            DECIMAL(19, 4) NOT NULL DEFAULT 0.0000,

    available_balance  DECIMAL(19, 4) NOT NULL DEFAULT 0.0000,

    created_date       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    last_modified_date TIMESTAMP      NOT NULL
                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_accounts_account_number
        UNIQUE (account_number),

    INDEX              idx_accounts_customer_id (customer_id),
    INDEX              idx_accounts_status (status),
    INDEX              idx_accounts_account_type (account_type)
);