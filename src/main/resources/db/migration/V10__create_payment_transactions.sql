CREATE TABLE payment_transactions (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    subscription_id     BIGINT        NOT NULL REFERENCES subscriptions (id),
    amount              DECIMAL(10,2) NOT NULL,
    currency            VARCHAR(3)    NOT NULL,
    external_reference  VARCHAR(120)  NOT NULL,
    is_successful       BOOLEAN       NOT NULL,
    processed_at        TIMESTAMP     NOT NULL,
    CONSTRAINT uk_payment_transactions_external_reference UNIQUE (external_reference)
);
