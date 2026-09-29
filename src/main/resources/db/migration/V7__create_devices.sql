CREATE TABLE devices (
    id                       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    plot_id                  BIGINT       REFERENCES plots (id),
    activation_code          VARCHAR(40)  NOT NULL,
    status                   VARCHAR(20)  NOT NULL,
    calibration_factor       DECIMAL(6,4) NOT NULL DEFAULT 1.0,
    battery_level            INT,
    firmware_version         VARCHAR(20),
    reading_interval_minutes INT          NOT NULL DEFAULT 30,
    last_seen_at             TIMESTAMP,
    created_at               TIMESTAMP    NOT NULL,
    CONSTRAINT uk_devices_activation_code UNIQUE (activation_code),
    CONSTRAINT uk_devices_plot_id UNIQUE (plot_id),
    CONSTRAINT ck_devices_status CHECK (status IN ('UNASSIGNED','ACTIVE','OFFLINE','INACTIVE')),
    CONSTRAINT ck_devices_battery_range CHECK (battery_level IS NULL OR battery_level BETWEEN 0 AND 100)
);
