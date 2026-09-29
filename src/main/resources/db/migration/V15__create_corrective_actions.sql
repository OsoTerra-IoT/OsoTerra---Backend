CREATE TABLE corrective_actions (
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    salinity_alert_id  BIGINT       NOT NULL REFERENCES salinity_alerts (id),
    action_type        VARCHAR(40)  NOT NULL,
    executed_at        DATE         NOT NULL,
    notes              VARCHAR(500),
    registered_by      BIGINT       NOT NULL REFERENCES user_accounts (id),
    registered_at      TIMESTAMP    NOT NULL,
    CONSTRAINT uk_corrective_actions_salinity_alert UNIQUE (salinity_alert_id)
);
