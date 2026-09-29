CREATE TABLE farms (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    owner_id   BIGINT       NOT NULL REFERENCES user_accounts (id),
    name       VARCHAR(120) NOT NULL,
    department VARCHAR(60)  NOT NULL,
    province   VARCHAR(60)  NOT NULL,
    district   VARCHAR(60)  NOT NULL,
    created_at TIMESTAMP    NOT NULL
);
