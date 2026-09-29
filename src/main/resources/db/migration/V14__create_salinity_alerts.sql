CREATE TABLE salinity_alerts (
    id                          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    plot_id                     BIGINT       NOT NULL REFERENCES plots (id),
    soil_reading_id             BIGINT       NOT NULL REFERENCES soil_readings (id),
    observed_conductivity_ds_m  DECIMAL(6,3) NOT NULL,
    applied_threshold_ds_m      DECIMAL(5,2) NOT NULL,
    excess_ratio                DECIMAL(6,4) NOT NULL,
    severity                    VARCHAR(20)  NOT NULL,
    status                      VARCHAR(20)  NOT NULL,
    acknowledged_by             BIGINT       REFERENCES user_accounts (id),
    acknowledged_at             TIMESTAMP,
    generated_at                TIMESTAMP    NOT NULL,
    CONSTRAINT ck_salinity_alerts_severity CHECK (severity IN ('WATCH','WARNING','CRITICAL')),
    CONSTRAINT ck_salinity_alerts_status CHECK (status IN ('OPEN','ACKNOWLEDGED','RESOLVED'))
);
CREATE UNIQUE INDEX uk_salinity_alerts_open_severity
    ON salinity_alerts (plot_id, severity)
    WHERE status = 'OPEN';
