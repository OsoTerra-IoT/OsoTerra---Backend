CREATE TABLE user_accounts (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email                VARCHAR(120) NOT NULL,
    password_hash        VARCHAR(255) NOT NULL,
    hash_algorithm       VARCHAR(60)  NOT NULL,
    first_name           VARCHAR(80)  NOT NULL,
    last_name            VARCHAR(80)  NOT NULL,
    role                 VARCHAR(20)  NOT NULL,
    professional_license VARCHAR(30),
    is_active            BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at           TIMESTAMP    NOT NULL,
    updated_at           TIMESTAMP,
    CONSTRAINT uk_user_accounts_email UNIQUE (email),
    CONSTRAINT ck_user_accounts_role CHECK (role IN ('FARMER', 'ADVISOR'))
);
