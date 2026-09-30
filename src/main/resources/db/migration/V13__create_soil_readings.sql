CREATE TABLE soil_readings (
    id                            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    device_id                     BIGINT       NOT NULL REFERENCES devices (id),
    plot_id                       BIGINT       NOT NULL REFERENCES plots (id),
    reading_batch_id              BIGINT       REFERENCES reading_batches (id),
    raw_conductivity_ds_m         DECIMAL(6,3) NOT NULL,
    compensated_conductivity_ds_m DECIMAL(6,3) NOT NULL,
    compensation_factor           DECIMAL(6,4) NOT NULL,
    moisture_percentage           DECIMAL(5,2) NOT NULL,
    temperature_celsius           DECIMAL(5,2) NOT NULL,
    captured_at                   TIMESTAMP    NOT NULL,
    stored_at                     TIMESTAMP    NOT NULL,
    CONSTRAINT uk_soil_readings_device_captured UNIQUE (device_id, captured_at),
    CONSTRAINT ck_soil_readings_raw_nonneg CHECK (raw_conductivity_ds_m >= 0),
    CONSTRAINT ck_soil_readings_compensated_nonneg CHECK (compensated_conductivity_ds_m >= 0),
    CONSTRAINT ck_soil_readings_moisture_range CHECK (moisture_percentage BETWEEN 0 AND 100)
);
CREATE INDEX ix_soil_readings_plot_captured ON soil_readings (plot_id, captured_at);
