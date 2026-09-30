CREATE TABLE subscription_plans (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(60)   NOT NULL,
    price_amount   DECIMAL(10,2) NOT NULL,
    price_currency VARCHAR(3)    NOT NULL DEFAULT 'PEN',
    billing_cycle  VARCHAR(20)   NOT NULL,
    max_plots      INT           NOT NULL,
    is_free        BOOLEAN       NOT NULL DEFAULT FALSE,
    is_active      BOOLEAN       NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_subscription_plans_name UNIQUE (name),
    CONSTRAINT ck_subscription_plans_billing_cycle CHECK (billing_cycle IN ('MONTHLY','ANNUAL','NONE'))
);
