CREATE TABLE password_reset_tokens (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_account_id BIGINT       NOT NULL REFERENCES user_accounts (id),
    token           VARCHAR(255) NOT NULL,
    expires_at      TIMESTAMP    NOT NULL,
    is_used         BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_password_reset_tokens_token UNIQUE (token)
);
