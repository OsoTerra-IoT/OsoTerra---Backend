CREATE TABLE salinity_trends (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    plot_id             BIGINT       NOT NULL REFERENCES plots (id),
    period_start_date   DATE         NOT NULL,
    period_end_date     DATE         NOT NULL,
    slope_ds_m_per_day  DECIMAL(8,5) NOT NULL,
    direction           VARCHAR(20)  NOT NULL,
    reading_count       INT          NOT NULL,
    computed_at         TIMESTAMP    NOT NULL,
    CONSTRAINT ck_salinity_trends_period CHECK (period_end_date > period_start_date),
    CONSTRAINT ck_salinity_trends_direction CHECK (direction IN ('RISING','STABLE','FALLING')),
    CONSTRAINT ck_salinity_trends_reading_count CHECK (reading_count >= 30)
);
