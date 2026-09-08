CREATE TABLE account_status_history
(
    id              BINARY(16) NOT NULL,

    account_id      BINARY(16) NOT NULL,

    previous_status VARCHAR(30),

    new_status      VARCHAR(30) NOT NULL,

    reason          VARCHAR(255),

    changed_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_status_history_account
        FOREIGN KEY (account_id)
            REFERENCES accounts (id),

    INDEX           idx_status_history_account_id (account_id)
);