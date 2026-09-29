CREATE TABLE notification_preferences (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_account_id     BIGINT      NOT NULL REFERENCES user_accounts (id),
    minimum_severity    VARCHAR(20) NOT NULL DEFAULT 'WATCH',
    channel             VARCHAR(20) NOT NULL,
    push_device_token   VARCHAR(255),
    preferred_language  VARCHAR(10) NOT NULL DEFAULT 'en_US',
    CONSTRAINT uk_notification_preferences_user UNIQUE (user_account_id),
    CONSTRAINT ck_notification_preferences_channel CHECK (channel IN ('PUSH','EMAIL','BOTH'))
);
