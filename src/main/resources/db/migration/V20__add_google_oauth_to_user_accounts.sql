ALTER TABLE user_accounts
    ALTER COLUMN password_hash DROP NOT NULL,
    ALTER COLUMN hash_algorithm DROP NOT NULL,
    ADD COLUMN google_account_id VARCHAR(255),
    ADD CONSTRAINT uk_user_accounts_google_account_id UNIQUE (google_account_id),
    ADD CONSTRAINT ck_user_accounts_auth_method CHECK (password_hash IS NOT NULL OR google_account_id IS NOT NULL);
