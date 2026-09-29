CREATE TABLE calibration_records (
    id                          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    device_id                   BIGINT       NOT NULL REFERENCES devices (id),
    lab_conductivity_ds_m       DECIMAL(6,3) NOT NULL,
    sampling_date               DATE         NOT NULL,
    laboratory_name             VARCHAR(120) NOT NULL,
    device_reading_at_sampling  DECIMAL(6,3) NOT NULL,
    resulting_factor            DECIMAL(6,4) NOT NULL,
    registered_at               TIMESTAMP    NOT NULL
);
