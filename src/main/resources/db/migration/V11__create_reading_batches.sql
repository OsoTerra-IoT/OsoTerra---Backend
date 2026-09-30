CREATE TABLE reading_batches (
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    device_id        BIGINT      NOT NULL REFERENCES devices (id),
    status           VARCHAR(20) NOT NULL,
    submitted_at     TIMESTAMP   NOT NULL,
    accepted_count   INT         NOT NULL DEFAULT 0,
    discarded_count  INT         NOT NULL DEFAULT 0,
    CONSTRAINT ck_reading_batches_status CHECK (status IN ('PENDING','SYNCHRONIZED','DISCARDED'))
);
