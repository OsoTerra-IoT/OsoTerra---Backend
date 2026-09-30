CREATE TABLE plot_reports (
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    plot_id            BIGINT    NOT NULL REFERENCES plots (id),
    generated_by       BIGINT    NOT NULL REFERENCES user_accounts (id),
    period_start_date  DATE      NOT NULL,
    period_end_date    DATE      NOT NULL,
    generated_at       TIMESTAMP NOT NULL
);
