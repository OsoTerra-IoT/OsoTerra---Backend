CREATE TABLE subscriptions (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_account_id       BIGINT      NOT NULL REFERENCES user_accounts (id),
    subscription_plan_id  BIGINT      NOT NULL REFERENCES subscription_plans (id),
    status                VARCHAR(20) NOT NULL,
    quota_total           INT         NOT NULL,
    quota_consumed        INT         NOT NULL DEFAULT 0,
    period_start_date     DATE        NOT NULL,
    period_end_date       DATE,
    created_at            TIMESTAMP   NOT NULL,
    CONSTRAINT ck_subscriptions_status CHECK (status IN ('ACTIVE','PENDING_PAYMENT','SUSPENDED','CANCELLED')),
    CONSTRAINT ck_subscriptions_quota CHECK (quota_consumed <= quota_total)
);
