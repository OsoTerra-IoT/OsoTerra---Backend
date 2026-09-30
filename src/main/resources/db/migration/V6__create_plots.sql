CREATE TABLE plots (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    farm_id       BIGINT        NOT NULL REFERENCES farms (id),
    crop_id       BIGINT        REFERENCES crops (id),
    name          VARCHAR(120)  NOT NULL,
    area_hectares DECIMAL(8,2)  NOT NULL,
    latitude      DECIMAL(10,7) NOT NULL,
    longitude     DECIMAL(10,7) NOT NULL,
    is_active     BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP     NOT NULL,
    CONSTRAINT ck_plots_area_positive CHECK (area_hectares > 0)
);
